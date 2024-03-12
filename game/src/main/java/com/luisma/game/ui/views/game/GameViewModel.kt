package com.luisma.game.ui.views.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisma.core.models.BasicScreenState
import com.luisma.core.models.WDuration
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core.services.RoutePayload
import com.luisma.core.services.RouterService
import com.luisma.core.services.Routes
import com.luisma.game.models.GameViewType
import com.luisma.game.models.NextWordDuration
import com.luisma.game.models.PlayingWord
import com.luisma.game.models.PlayingWordGameState
import com.luisma.game.models.UNBLOCK_WOD
import com.luisma.game.models.WCharAnimationState
import com.luisma.game.models.WCharRowAnimationState
import com.luisma.game.models.WKeyboardKeyState
import com.luisma.game.services.GameUtilsService
import com.luisma.game.services.PlayingWordService
import com.luisma.game.services.UserStatsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val playingWordService: PlayingWordService,
    private val gameUtilsService: GameUtilsService,
    private val userStatsService: UserStatsService,
    private val routerService: RouterService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(GameViewState.default())
    val state = _state.asStateFlow()

    init {
        val wordId = getNavigationWordId()
        if (wordId == null) {
            viewModelScope.launch {
                showTutorialOnFirstGame()
                startWOD()
            }
        } else {
            viewModelScope.launch {
                startHistoricWord(wordId)
            }
        }
    }

    fun sendEvent(event: GameViewEvents) {
        when (event) {
            is GameViewEvents.Enter -> onEnter()
            is GameViewEvents.DeleteChar -> deleteLastChar()
            is GameViewEvents.FinishHorizontalAnimation -> resetHorizontalRowAnimation()
            is GameViewEvents.GoToHistoric -> goToHistoric()
            is GameViewEvents.GoBack -> goBack()
            is GameViewEvents.SendChar -> sendChar(event.char)
            is GameViewEvents.HandleStats -> handleStats(event.showStats)
            is GameViewEvents.HandleTutorial -> handleTutorial(event.showTutorial)
            is GameViewEvents.FinishAppearAnimation -> resetAppearAnimation(
                rowIdx = event.rowIdx,
                columnIdx = event.columnIdx
            )

            is GameViewEvents.FinishRowAnimation -> resetRowAnimation(
                rowIdx = event.rowIdx
            )

        }
    }

    //region events methods
    private fun onEnter() {
        val guessedWord = gameUtilsService.wordFromWCharsToString(
            _state.value.playingRow
        )
        showWOD(guessedWord)
        viewModelScope.launch {
            val exists = playingWordService.resolveGuessedWord(
                guessedWord = guessedWord,
            )
            if (!exists) {
                _state.update {
                    it.copy(
                        horizontalAnimationState = WCharRowAnimationState.TriggerHorizontal
                    )
                }
                return@launch
            }
            // resolve new LettersRows
            val wordsWithSeparators = gameUtilsService.lettersRowsToStringWithSeparator(
                lettersRows = _state.value.playingWord.lettersRows
            )
            // previous cursor position
            var prevPlayingRowIdx = _state.value.gameCursorPosition.row - 1
            prevPlayingRowIdx = if (prevPlayingRowIdx <= 0) 0 else prevPlayingRowIdx
            // resolve new letter rows
            var newLetterRows = gameUtilsService.resolveLettersState(
                wordsWithSeparators = wordsWithSeparators,
                toGuessWord = _state.value.playingWord.word,
            )
            //  check game state
            val newGameState = gameUtilsService.getPlayingWordGameState(
                newLetterRows
            )
            // update currently playing db
            playingWordService.updateCurrentlyPlayingWord(
                wordsWithSeparators = wordsWithSeparators,
                wordId = _state.value.playingWord.wordId,
                playingState = getPlayingStateByGameState(newGameState)
            )
            // update state with playing word
            when (newGameState) {
                // win or lose, keyboard and cursor not need to be updated,
                // when lose in historic mode, the keyboard, will be still open
                // but with all the keys disabled, check [GameState.showKeyboard]
                PlayingWordGameState.Win, PlayingWordGameState.Lose -> {
                    // set user stats
                    if (_state.value.gameType == GameViewType.WOD) {
                        userStatsService.setUserStats(
                            doneAtTry = _state.value.gameCursorPosition.row,
                            isAWin = newGameState == PlayingWordGameState.Win
                        )
                    }
                    // keyboard state exception
                    val enabledKeyState = if (
                        newGameState == PlayingWordGameState.Win
                        && _state.value.gameType == GameViewType.Historic
                    ) {
                        WKeyboardKeyState.allDisabled()
                    } else {
                        _state.value.enabledKeyState
                    }
                    // set animation win or lose
                    val animationState = if (newGameState == PlayingWordGameState.Lose
                    ) {
                        WCharAnimationState.Reveal
                    } else {
                        WCharAnimationState.Success
                    }
                    newLetterRows = gameUtilsService.putAnimationStateToRow(
                        rowIdx = _state.value.gameCursorPosition.row,
                        animationState = animationState,
                        lettersRows = newLetterRows,
                    )
                    // build new playing word
                    val newPlayingWord = _state.value.playingWord.copy(
                        state = newGameState,
                        lettersRows = newLetterRows
                    )
                    _state.update {
                        it.copy(
                            playingWord = newPlayingWord,
                            correctWordWithState = gameUtilsService.getCorrectWordWithState(
                                newPlayingWord
                            ),
                            enabledKeyState = enabledKeyState
                        )
                    }
                }
                //incomplete, update keyboard and cursor
                PlayingWordGameState.Incomplete -> {
                    // clear playing row
                    val clearPlayingRow = GameViewState.default().playingRow
                    // unset prev animation
                    newLetterRows = gameUtilsService.putAnimationStateToRow(
                        rowIdx = prevPlayingRowIdx,
                        lettersRows = newLetterRows,
                        animationState = WCharAnimationState.Still,
                    )
                    // set current animation
                    newLetterRows = gameUtilsService.putAnimationStateToRow(
                        rowIdx = _state.value.gameCursorPosition.row,
                        lettersRows = newLetterRows,
                        animationState = WCharAnimationState.Reveal
                    )
                    // build new playing word
                    val newPlayingWord = _state.value.playingWord.copy(
                        state = newGameState,
                        lettersRows = newLetterRows
                    )
                    _state.update {
                        it.copy(
                            playingWord = newPlayingWord,
                            keyboard = gameUtilsService.resolveFullKeyboard(
                                newPlayingWord.lettersRows
                            ),
                            gameCursorPosition = gameUtilsService.getCursorPosition(
                                newPlayingWord.lettersRows
                            ),
                            enabledKeyState = gameUtilsService.getEnabledKeyStateFromPlayingRow(
                                clearPlayingRow
                            ),
                            playingRow = clearPlayingRow
                        )
                    }
                }
            }
        }
    }

    private fun deleteLastChar() {
        val response = gameUtilsService.removeLastChar(
            playingRow = _state.value.playingRow,
            lettersRow = _state.value.playingWord.lettersRows,
        )
        _state.update {
            it.copy(
                playingRow = response.listChars,
                gameCursorPosition = response.cursorPosition,
                enabledKeyState = response.enabledKeyState,
                playingWord = _state.value.playingWord.copy(
                    lettersRows = response.letterRows,
                ),
                horizontalAnimationState = WCharRowAnimationState.Still,
            )
        }
    }

    private fun resetHorizontalRowAnimation() {
        _state.update {
            it.copy(
                horizontalAnimationState = WCharRowAnimationState.Still
            )
        }
    }

    private fun goToHistoric() {
        viewModelScope.launch {
            routerService.goTo(RoutePayload(route = Routes.Historic))
        }
    }

    private fun goBack() {
        viewModelScope.launch {
            routerService.goBack()
        }
    }

    private fun sendChar(char: Char) {
        val response = gameUtilsService.addChar(
            playingRow = _state.value.playingRow,
            newChar = char,
            lettersRow = _state.value.playingWord.lettersRows
        )
        _state.update {
            it.copy(
                playingRow = response.listChars,
                gameCursorPosition = response.cursorPosition,
                enabledKeyState = response.enabledKeyState,
                playingWord = _state.value.playingWord.copy(
                    lettersRows = response.letterRows,
                ),
            )
        }
    }

    private fun handleStats(show: Boolean) {
        _state.update {
            it.copy(showStats = show)
        }
    }

    private fun handleTutorial(show: Boolean) {
        _state.update {
            it.copy(showTutorial = show)
        }
    }

    private fun resetAppearAnimation(
        rowIdx: Int,
        columnIdx: Int
    ) {
        val playingWord = _state.value.playingWord.copy(
            lettersRows = gameUtilsService.putAnimationStateCell(
                rowIdx = rowIdx,
                columnIdx = columnIdx,
                lettersRows = _state.value.playingWord.lettersRows
            )
        )
        _state.update {
            it.copy(playingWord = playingWord)
        }
    }

    private fun resetRowAnimation(rowIdx: Int) {
        val newLetterRows = gameUtilsService.putAnimationStateToRow(
            rowIdx = rowIdx,
            animationState = WCharAnimationState.Still,
            lettersRows = _state.value.playingWord.lettersRows,
        )
        val newPlayingWord = _state.value.playingWord.copy(
            lettersRows = newLetterRows
        )
        _state.update {
            it.copy(playingWord = newPlayingWord)
        }
    }
    //endregion

    //region control methods
    private suspend fun showTutorialOnFirstGame() {
        if (!userStatsService.isFirstPlay()) {
            return
        }
        userStatsService.unCheckIsFirstPlay()
        _state.update {
            it.copy(showTutorial = true)
        }
    }

    private suspend fun startWOD() {
        val playingWord = playingWordService.getCurrentlyPlaying()
        // no wod, the game is finish
        if (playingWord == null) {
            _state.update {
                it.copy(
                    gameType = GameViewType.WOD,
                    gameFinished = true,
                    screenState = BasicScreenState.Success
                )
            }
            return
        }
        startGame(
            playingWord = playingWord,
            gameViewType = GameViewType.WOD,
        )
        startTimerForWOD(playingWord.wordId)
    }

    private suspend fun startHistoricWord(wordId: Int) {
        val playingWord = playingWordService.getUserWordById(wordId)
        if (playingWord == null) {
            "This should never happen, in historic mode the word is in the user db"
            return
        }
        startGame(
            playingWord = playingWord,
            gameViewType = GameViewType.Historic,
        )
    }

    private fun startGame(
        playingWord: PlayingWord,
        gameViewType: GameViewType,
    ) {
        // done
        if (playingWord.state == PlayingWordGameState.Win ||
            playingWord.state == PlayingWordGameState.Lose
        ) {
            _state.update {
                it.copy(
                    gameType = gameViewType,
                    screenState = BasicScreenState.Success,
                    playingWord = playingWord,
                    correctWordWithState = gameUtilsService.getCorrectWordWithState(playingWord)
                )
            }
            return
        }

        // incomplete
        _state.update {
            val cursorPosition = gameUtilsService.getCursorPosition(
                playingWord.lettersRows
            )
            it.copy(
                gameType = gameViewType,
                screenState = BasicScreenState.Success,
                playingWord = playingWord,
                keyboard = gameUtilsService.resolveFullKeyboard(
                    playingWord.lettersRows
                ),
                gameCursorPosition = cursorPosition,
                enabledKeyState = gameUtilsService.getEnabledKeyStateFromPlayingRow(
                    _state.value.playingRow
                ),
            )
        }
    }

    private suspend fun startTimerForWOD(wordID: Int) {
        playingWordService.countdownNextWOD(wordID)
            .takeWhile { duration ->
                if (duration == WDuration.zero()) {
                    // reset state except game type
                    _state.update {
                        GameViewState.default().copy(
                            gameType = it.gameType,
                        )
                    }
                    // security second
                    delay(1000)
                    // start again
                    startWOD()
                }
                return@takeWhile duration != WDuration.zero()
            }
            .collect { duration ->
                _state.update {
                    it.copy(
                        nextWODDuration = NextWordDuration.fromWDuration(duration)
                    )
                }
            }
    }

    private fun getPlayingStateByGameState(
        newGameState: PlayingWordGameState
    ): UserWordsPlayingStateContract {
        if (newGameState == PlayingWordGameState.Win
            && _state.value.gameType == GameViewType.WOD
        ) {
            return UserWordsPlayingStateContract.SOLVED_IN_TIME
        }

        if (newGameState == PlayingWordGameState.Win
            && _state.value.gameType == GameViewType.Historic
        ) {
            return UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME
        }

        if (newGameState == PlayingWordGameState.Lose) {
            return UserWordsPlayingStateContract.LOSE
        }

        return UserWordsPlayingStateContract.PLAYING
    }

    private fun showWOD(guessedWord: String) {
        if (guessedWord != UNBLOCK_WOD || _state.value.showWOD) {
            return
        }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    showWOD = true
                )
            }
            delay(1000)
            _state.update {
                it.copy(
                    showWOD = false
                )
            }
        }
    }

    private fun getNavigationWordId(): Int? {
        val wordID: String? = savedStateHandle[
            Routes.GameHistoric.payloadName ?: ""
        ]
        return try {
            wordID?.toInt() ?: return null
        } catch (e: Exception) {
            null
        }
    }
    //endregion control

}




