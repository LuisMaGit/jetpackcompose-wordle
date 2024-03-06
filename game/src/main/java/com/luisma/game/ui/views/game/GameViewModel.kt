package com.luisma.game.ui.views.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisma.core.models.BasicScreenState
import com.luisma.core.models.WDuration
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.game.models.GameEnabledKeyState
import com.luisma.game.models.NextWordDuration
import com.luisma.game.models.PlayingWordGameState
import com.luisma.game.models.UNBLOCK_WOD
import com.luisma.game.models.WCharAnimationState
import com.luisma.game.models.WCharRowAnimationState
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
    private val userStatsService: UserStatsService
) : ViewModel() {

    private val _state = MutableStateFlow(GameState.default())
    val state = _state.asStateFlow()

    init {
        when (_state.value.gameType) {
            GameType.WOD -> viewModelScope.launch {
                startWOD()
            }

            GameType.Historic -> TODO(reason = "IMPLEMENT THIS")
        }
    }

    fun sendEvent(event: GameEvents) {
        when (event) {
            is GameEvents.Enter -> onEnter()
            is GameEvents.DeleteChar -> deleteLastChar()
            is GameEvents.FinishHorizontalAnimation -> resetHorizontalRowAnimation()
            is GameEvents.SendChar -> sendChar(event.char)
            is GameEvents.HandleStats -> handleStats(event.showStats)
            is GameEvents.HandleTutorial -> handleTutorial(event.showTutorial)
            is GameEvents.FinishAppearAnimation -> resetAppearAnimation(
                rowIdx = event.rowIdx,
                columnIdx = event.columnIdx
            )

            is GameEvents.FinishRowAnimation -> resetRowAnimation(
                rowIdx = event.rowIdx
            )

        }
    }

    //region events
    private suspend fun startWOD() {
        val playingWord = playingWordService.getCurrentlyPlaying()

        // no wod
        if (playingWord == null) {
            TODO(
                reason = """"This should happen only when all words available for WOD are completed,
                    Show a sign or something similar and break
                """"
            )
        }
        // wod done
        if (playingWord.state == PlayingWordGameState.Win ||
            playingWord.state == PlayingWordGameState.Lose
        ) {
            _state.update {
                it.copy(
                    screenState = BasicScreenState.Success,
                    playingWord = playingWord,
                    correctWordWithState = gameUtilsService.getCorrectWordWithState(playingWord)
                )
            }
            startTimerForWOD(playingWord.wordId)
            return
        }
        // incomplete
        _state.update {
            val cursorPosition = gameUtilsService.getCursorPosition(
                playingWord.lettersRows
            )
            it.copy(
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
        startTimerForWOD(playingWord.wordId)
    }

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
                    if (_state.value.gameType == GameType.WOD) {
                        userStatsService.setUserStats(
                            doneAtTry = _state.value.gameCursorPosition.row,
                            isAWin = newGameState == PlayingWordGameState.Win
                        )
                    }
                    // keyboard state exception
                    val enabledKeyState = if (
                        newGameState == PlayingWordGameState.Win
                        && _state.value.gameType == GameType.Historic
                    ) {
                        GameEnabledKeyState.allDisabled()
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
                    val clearPlayingRow = GameState.default().playingRow
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

    private suspend fun startTimerForWOD(wordID: Int) {
        playingWordService.countdownNextWOD(wordID)
            .takeWhile { duration ->
                if (duration == WDuration.zero()) {
                    // reset state except game type
                    _state.update {
                        GameState.default().copy(
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
            && _state.value.gameType == GameType.WOD
        ) {
            return UserWordsPlayingStateContract.SOLVED_IN_TIME
        }

        if (newGameState == PlayingWordGameState.Win
            && _state.value.gameType == GameType.Historic
        ) {
            return UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME
        }

        if(newGameState == PlayingWordGameState.Lose){
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

}




