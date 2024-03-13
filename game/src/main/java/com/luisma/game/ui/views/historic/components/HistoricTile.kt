package com.luisma.game.ui.views.historic.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core_ui.R
import com.luisma.core_ui.components.CharBox
import com.luisma.core_ui.components.CharBoxDimensions
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.components.WTextType
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.models.GameDate
import com.luisma.game.models.ListCharsWithState
import com.luisma.game.models.UserHistoricWord
import com.luisma.game.models.WChar
import com.luisma.game.models.WCharState
import com.luisma.game.ui.utils.fullDataUIMapper
import com.luisma.game.ui.utils.gameStateTextColorUIMapper
import com.luisma.game.ui.utils.uiCharStateMapper
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HistoricTile(
    modifier: Modifier = Modifier,
    index: Int,
    word: UserHistoricWord,
    boxDimensions: CharBoxDimensions,
    onTap: (index: Int) -> Unit
) {

    val boxSpacing = WSpacing.k5
    val charsCount = word.lastChars.chars.count()
    val charsWidth = boxDimensions.size * charsCount + (boxSpacing * (charsCount - 1))

    fun gameStateTextUIMapper(state: UserWordsPlayingStateContract): Int {
        return when (state) {
            UserWordsPlayingStateContract.PLAYING -> R.string.historic_tile_playing
            UserWordsPlayingStateContract.LOSE -> R.string.historic_tile_lose
            UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME -> R.string.historic_tile_not_on_time
            UserWordsPlayingStateContract.SOLVED_IN_TIME -> R.string.historic_tile_on_time
        }
    }

    Column(
        modifier = modifier.clickable {
            onTap(index)
        }
    ) {
        // boxes
        Row {
            word.lastChars.chars.forEachIndexed { index, wChar ->
                CharBox(
                    modifier = Modifier.padding(
                        end = if (index == charsCount - 1) {
                            0.dp
                        } else {
                            boxSpacing
                        }
                    ),
                    charState = uiCharStateMapper(wChar.state),
                    char = wChar.char,
                )
            }
        }
        // state text + try ratio
        Row(
            modifier = Modifier
                .width(charsWidth),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // state text
            WText(
                text = stringResource(id = gameStateTextUIMapper(word.state)),
                wTextType = WTextType.T2,
                color = gameStateTextColorUIMapper(
                    state = word.state,
                    wColors = WTheme.colors
                )
            )
            // try ratio
            WText(
                text = "${word.tryNumber}/${word.maxTries}",
                wTextType = WTextType.T2,
                color = WTheme.colors.placeholderFill
            )
        }
        // date
        Box(
            modifier = Modifier
                .width(charsWidth),
            contentAlignment = Alignment.CenterEnd
        ) {
            WText(
                text = fullDataUIMapper(word.date),
                wTextType = WTextType.T2,
                color = WTheme.colors.placeholderFill
            )
        }
    }

}

@Preview
@Composable
private fun HistoricSuccessPrev() {
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color = WTheme.colors.background
        ) {
            HistoricTile(
                index = 0,
                word = UserHistoricWord(
                    lastChars = ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(state = WCharState.RightPlace, char = 'W'),
                            WChar(state = WCharState.WrongPlace, char = 'X'),
                            WChar(state = WCharState.NoMatch, char = 'Z'),
                            WChar(state = WCharState.WrongPlace, char = 'Y'),
                            WChar(state = WCharState.RightPlace, char = 'V'),
                        ),
                    ),
                    state = UserWordsPlayingStateContract.PLAYING,
                    date = GameDate.noTime().copy(
                        isToday = true
                    ),
                    maxTries = 6,
                    tryNumber = 3,
                    wordId = 0,
                    isWOD = false
                ),
                boxDimensions = CharBoxDimensions.small(),
                onTap = {}
            )
        }
    }
}