package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.R
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.components.WTextType
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.core_ui.theme.WTypeSizeContract
import com.luisma.game.models.NextWordDuration

@Composable
internal fun GameBigTimer(
    nextWordDuration: NextWordDuration,
) {
    Row {
        //h
        Time(
            modifier = Modifier.alignByBaseline(),
            value = nextWordDuration.hoursStr
        )
        Letters(
            modifier = Modifier.alignByBaseline(),
            value = stringResource(
                R.string.game_next_word_time_h,
            )
        )
        Separator(modifier = Modifier.alignByBaseline())
        //m
        Time(
            modifier = Modifier.alignByBaseline(),
            value = nextWordDuration.minStr
        )
        Letters(
            modifier = Modifier.alignByBaseline(),
            value = stringResource(
                R.string.game_next_word_time_m,
            )
        )
        Separator(modifier = Modifier.alignByBaseline())
        //s
        Time(
            modifier = Modifier.alignByBaseline(),
            value = nextWordDuration.secStr
        )
        Letters(
            modifier = Modifier.alignByBaseline(),
            value = stringResource(
                R.string.game_next_word_time_s,
            )
        )
    }
}


@Composable
private fun Letters(
    modifier: Modifier,
    value: String
) {
    WText(
        modifier = modifier,
        wTextType = WTextType.T1,
        color = WTheme.colors.placeholderFill,
        fontSize = WTypeSizeContract.k32,
        text = value,
    )
}

@Composable
private fun Time(
    modifier: Modifier,
    value: String
) {
    WText(
        modifier = modifier,
        wTextType = WTextType.T1,
        fontSize = WTypeSizeContract.k32,
        text = value,
    )
}

@Composable
private fun Separator(
    modifier: Modifier,
) {
    Time(
        modifier = modifier,
        value = " : ",
    )
}


@Composable
@Preview(
    showBackground = true
)
private fun GameBigTimerPreview() {
    WThemeProvider {
        GameBigTimer(
            nextWordDuration = NextWordDuration.zero().copy(
                hoursStr = "23",
                minStr = "02",
                secStr = "38"
            )
        )
    }
}