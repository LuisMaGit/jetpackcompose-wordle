package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.R
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.components.WTextType
import com.luisma.core_ui.theme.WFontSize
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.models.NextWordDuration


@Composable
internal fun GameGridFooterWordNumber(
    playingWordNumb: Int,
    endPadding: Dp = 0.dp,
) {
    GameGridFooterWrapper(
        endPadding = endPadding,
    ) {
        FooterText(
            text = stringResource(
                R.string.game_word_numb,
                playingWordNumb
            )
        )
    }
}

@Composable
internal fun GameGridFooterNextWordTimer(
    nextWordDuration: NextWordDuration,
    endPadding: Dp = 0.dp,
) {
    GameGridFooterWrapper(
        endPadding = endPadding,
    ) {
        FooterText(
            text = stringResource(
                R.string.game_word_next_word_timer,
            )
                    + " "
                    + nextWordDuration.hoursStr + stringResource(
                R.string.game_next_word_time_h,
            )
                    + ":"
                    + nextWordDuration.minStr + stringResource(
                R.string.game_next_word_time_m,
            )
                    + ":"
                    + nextWordDuration.secStr + stringResource(
                R.string.game_next_word_time_s,
            )
        )
    }

}

@Composable
internal fun GameGridFooterWODCheat(
    wod: String,
    endPadding: Dp = 0.dp,
) {
    GameGridFooterWrapper(
        endPadding = endPadding,
    ) {
        FooterText(
            text = wod
        )
    }
}


@Composable
private fun GameGridFooterWrapper(
    endPadding: Dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = endPadding
            ),
        contentAlignment = Alignment.CenterEnd,
    ) {
        content()
    }
}


@Composable
private fun FooterText(
    text: String
) {
    WText(
        text = text,
        wTextType = WTextType.T2,
        fontSize = WFontSize.k12,
        color = WTheme.colors.placeholderBorder
    )
}


@Preview
@Composable
private fun GameGridFooterPreview() {
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color = WTheme.colors.background
        ) {
            Column {
                GameGridFooterWordNumber(
                    playingWordNumb = 20
                )
                GameGridFooterNextWordTimer(
                    nextWordDuration = NextWordDuration.zero()
                )
            }
        }
    }
}