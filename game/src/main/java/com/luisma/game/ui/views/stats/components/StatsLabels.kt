package com.luisma.game.ui.views.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.R
import com.luisma.core_ui.components.StatIndicator
import com.luisma.core_ui.theme.WThemeProvider

@Composable
internal fun StatsLabels(
    modifier: Modifier = Modifier,
    played: Int,
    percentWin: Int,
    currentStreak: Int,
    recordStreak: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // played
        StatIndicator(
            title = stringResource(id = R.string.stats_played),
            value = played.toString()
        )
        // percent win
        StatIndicator(
            title = stringResource(id = R.string.stats_wins),
            value = "$percentWin%"
        )
        // streak
        StatIndicator(
            title = stringResource(id = R.string.stats_streak),
            value = currentStreak.toString()
        )
        // record streak
        StatIndicator(
            title = stringResource(id = R.string.stats_streak_record),
            value = recordStreak.toString()
        )
    }
}


@Preview
@Composable
private fun StatsLabelsPrev() {
    WThemeProvider {
        StatsLabels(
            modifier = Modifier.fillMaxWidth(),
            played = 10,
            currentStreak = 12,
            percentWin = 1,
            recordStreak = 3
        )
    }
}