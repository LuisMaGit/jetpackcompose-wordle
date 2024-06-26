package com.luisma.game.ui.views.game.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.luisma.core.models.BasicScreenState
import com.luisma.core_ui.R
import com.luisma.core_ui.components.AdMobBanner
import com.luisma.core_ui.components.CharBoxDimensions
import com.luisma.core_ui.components.CharKeyDimensions
import com.luisma.core_ui.components.CharKeySubmitDimensions
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.components.WTextType
import com.luisma.core_ui.helpers.ScreenSizeHelper
import com.luisma.core_ui.theme.WFontSize
import com.luisma.core_ui.theme.WScreenFractions
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.models.GameViewType
import com.luisma.game.models.ListCharsWithState
import com.luisma.game.models.NextWordDuration
import com.luisma.game.models.PlayingWord
import com.luisma.game.models.PlayingWordGameState
import com.luisma.game.models.WChar
import com.luisma.game.models.WCharState
import com.luisma.game.models.WKeyboardKeyState
import com.luisma.game.ui.views.game.GameViewEvents
import com.luisma.game.ui.views.game.GameViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun GamePlay(
    state: GameViewState,
    events: (event: GameViewEvents) -> Unit,
) {
    val verticalScrollState = rememberScrollState()

    ScreenSizeHelper { screenSizeBreakPoints, screenWidth ->
        val charBoxDimensions = CharBoxDimensions.charBoxDimensionsByScreenSize(
            screenSizeBreakPoints
        )
        val letterRows = state.playingWord.lettersRows
        val columnsCount = letterRows.entries.first().value.chars.count()
        val rowsCount = letterRows.keys.count()

        fun getGridWidth(): Dp {
            if (columnsCount == 1) {
                return charBoxDimensions.size * columnsCount
            }
            return (charBoxDimensions.size * columnsCount) + (GameGridRowConstants.spacing * (columnsCount - 1))
        }

        fun getGridHeight(): Dp {
            if (rowsCount == 1) {
                return charBoxDimensions.size * rowsCount
            }
            return (charBoxDimensions.size * rowsCount) + (GameGridRowConstants.spacing * (rowsCount - 1))
        }

        val gridWidth = getGridWidth()
        val gridFooterEndPadding = (screenWidth - gridWidth) / 2

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    state = verticalScrollState
                )
        ) {
            GameAppbar(
                onTapStats = { events(GameViewEvents.HandleStats(showStats = true)) },
                onTapTutorial = { events(GameViewEvents.HandleTutorial(showTutorial = true)) },
                onTapHistory = { events(GameViewEvents.GoToHistoric) },
                onTapBack = { events(GameViewEvents.GoBack) },
                viewType = state.gameType
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k5))
            // grid
            GameGrid(
                modifier = Modifier
                    .size(
                        width = gridWidth,
                        height = getGridHeight()
                    ),
                gridData = letterRows,
                boxDimension = charBoxDimensions,
                horizontalRowAnimation = state.horizontalAnimationState,
                currentlyPlayingRowIdx = state.gameCursorPosition.row,
                onDismissHorizontalAnimation = {
                    events(GameViewEvents.FinishHorizontalAnimation)
                },
                onDismissScaleAnimation = { rowIdx, columnIdx ->
                    events(GameViewEvents.FinishAppearAnimation(rowIdx, columnIdx))
                },
                onDismissRowAnimation = { rowIdx ->
                    events(GameViewEvents.FinishRowAnimation(rowIdx))
                },
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k5))

            // word count
            GameGridFooterWordNumber(
                playingWordNumb = state.playingWord.wordNumber,
                endPadding = gridFooterEndPadding
            )

            // wod small timer
            if (state.showSmallTimer)
                GameGridFooterNextWordTimer(
                    nextWordDuration = state.nextWODDuration,
                    endPadding = gridFooterEndPadding
                )

            // cheat
            if (state.showWOD)
                GameGridFooterWODCheat(
                    wod = state.playingWord.word,
                    endPadding = gridFooterEndPadding
                )

            Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))

            // keyboard
            if (state.showKeyboard)
                GameKeyboard(
                    modifier = Modifier.padding(top = WSpacing.k40),
                    keyDimensions = CharKeyDimensions.keyDimensionsByScreenSize(
                        screenSizeBreakPoints
                    ),
                    keySubmitDimensions = CharKeySubmitDimensions.keySubmitDimensions(
                        screenSizeBreakPoints
                    ),
                    keyboard = state.keyboard,
                    onTapChar = { char ->
                        events(GameViewEvents.SendChar(char))
                    },
                    onTapDelete = {
                        events(GameViewEvents.DeleteChar)
                    },
                    onTapEnter = {
                        events(GameViewEvents.Enter)
                    },
                    enabledKeyState = state.enabledKeyState,
                )

            // correct word
            AnimatedVisibility(
                visible = state.showCorrectWord,
                enter = fadeIn(
                    animationSpec = tween(300, delayMillis = 300)
                )
            ) {
                GameCorrectWordAndHeader(
                    chars = state.correctWordWithState.chars,
                    charBoxDimensions = charBoxDimensions,
                )
            }

            // wod timer
            AnimatedVisibility(
                visible = state.showBigTimer,
                enter = fadeIn(
                    animationSpec = tween(300, delayMillis = 600)
                )
            ) {
                GameBigTimerAndHeader(
                    nextWordDuration = state.nextWODDuration
                )
            }

            // ads
            AdMobBanner(
                modifier = Modifier.padding(top = WSpacing.k20),
                adId = stringResource(id = R.string.add_mob_banner_id)
            )
        }
    }
}

@Composable
private fun GameCorrectWordAndHeader(
    chars: ImmutableList<WChar>,
    charBoxDimensions: CharBoxDimensions,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth(WScreenFractions.k80)
    ) {
        WText(
            text = stringResource(id = R.string.game_lose_title),
            wTextType = WTextType.T1,
            fontSize = WFontSize.k32,
            color = WTheme.colors.placeholderFill
        )
        WText(
            text = stringResource(id = R.string.game_lose_subtitle),
            wTextType = WTextType.T2,
            fontSize = WFontSize.k20,
            color = WTheme.colors.placeholderFill
        )
        Box {
            GameGridRow(
                chars = chars,
                boxDimension = charBoxDimensions
            )
        }
    }
}

@Composable
private fun GameBigTimerAndHeader(
    nextWordDuration: NextWordDuration,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth(WScreenFractions.k80)
    ) {
        WText(
            text = stringResource(id = R.string.game_timer_header),
            wTextType = WTextType.T2,
            fontSize = WFontSize.k20,
            color = WTheme.colors.placeholderFill
        )
        GameBigTimer(
            nextWordDuration = nextWordDuration
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun GamePlayPreview() {
    WThemeProvider(
        darkTheme = false
    ) {
        Surface(
            color = WTheme.colors.background
        ) {
            GamePlay(
                state = GameViewState.default()
                    .copy(
                        screenState = BasicScreenState.Success,
                        playingWord = PlayingWord.empty().copy(
                            lettersRows = gridMock,
                            wordNumber = 10,
                            state = PlayingWordGameState.Lose,
                            word = "ABCD"
                        ),
                        gameType = GameViewType.WOD,
                        showWOD = true,
                        nextWODDuration = NextWordDuration.zero(),
                        enabledKeyState = WKeyboardKeyState(
                            enabledDelete = true,
                            enableAdd = false,
                            enabledEnter = true,
                        ),
                        correctWordWithState = ListCharsWithState(
                            done = false,
                            chars = persistentListOf(
                                WChar(state = WCharState.RightPlace, char = 'A'),
                                WChar(state = WCharState.RightPlace, char = 'B'),
                                WChar(state = WCharState.RightPlace, char = 'C'),
                                WChar(state = WCharState.RightPlace, char = 'D'),
                                WChar(state = WCharState.RightPlace, char = 'E'),
                            )
                        )
                    ),
                events = {},
            )
        }
    }
}