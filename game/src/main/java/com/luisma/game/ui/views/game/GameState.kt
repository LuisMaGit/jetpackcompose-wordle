package com.luisma.game.ui.views.game

import com.luisma.core.models.BasicScreenState
import com.luisma.game.models.GameCursorPosition
import com.luisma.game.models.GameEnabledKeyState
import com.luisma.game.models.ListCharsWithState
import com.luisma.game.models.NextWordDuration
import com.luisma.game.models.PlayingWord
import com.luisma.game.models.PlayingWordGameState
import com.luisma.game.models.WCharRowAnimationState
import com.luisma.game.models.WKeyboard
import javax.annotation.concurrent.Immutable

@Immutable
data class GameState(
    val playingWord: PlayingWord,
    val gameType: GameType,
    val screenState: BasicScreenState,
    val correctWordWithState: ListCharsWithState,
    val showWOD: Boolean,
    // keyboard
    val keyboard: WKeyboard,
    val enabledKeyState: GameEnabledKeyState,
    // playing row
    val gameCursorPosition: GameCursorPosition,
    val playingRow: List<Char>,
    // animation
    val horizontalAnimationState: WCharRowAnimationState,
    // timer
    val nextWODDuration: NextWordDuration,
    val showStats: Boolean,
    val showTutorial: Boolean,
) {
    companion object {
        fun default(): GameState {
            return GameState(
                playingWord = PlayingWord.empty(),
                gameType = GameType.WOD,
                screenState = BasicScreenState.Loading,
                showWOD = false,
                nextWODDuration = NextWordDuration.zero(),
                keyboard = WKeyboard.initial(),
                gameCursorPosition = GameCursorPosition.initial(),
                playingRow = emptyList(),
                enabledKeyState = GameEnabledKeyState.allDisabled(),
                correctWordWithState = ListCharsWithState.empty(),
                horizontalAnimationState = WCharRowAnimationState.Still,
                showStats = false,
                showTutorial = false,
            )
        }
    }

    // big timer will only shows when WOD game is done
    val showBigTimer: Boolean
        get() {
            if (gameType != GameType.WOD) {
                return false
            }

            return playingWord.state == PlayingWordGameState.Lose
                    || playingWord.state == PlayingWordGameState.Win
        }

    // small timer will only shows while playing WOD
    val showSmallTimer: Boolean
        get() {
            if (gameType != GameType.WOD) {
                return false
            }

            return playingWord.state == PlayingWordGameState.Incomplete
        }

    // keyboard will only shows when it is while playing and historic win
    val showKeyboard: Boolean
        get() = playingWord.state == PlayingWordGameState.Incomplete
                || (playingWord.state == PlayingWordGameState.Win && gameType == GameType.Historic)

    // the correct word will only shows when the user loses
    val showCorrectWord: Boolean
        get() = playingWord.state == PlayingWordGameState.Lose

}

