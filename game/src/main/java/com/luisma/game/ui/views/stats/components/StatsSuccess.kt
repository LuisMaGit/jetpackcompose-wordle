package com.luisma.game.ui.views.stats.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.R
import com.luisma.core_ui.components.StatPercentBar
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.components.WTextType
import com.luisma.core_ui.theme.WFontSize
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.models.UserGameStatsWinDistribution
import com.luisma.game.models.UserGamesStats
import com.luisma.game.ui.views.historic.HistoricView
import com.luisma.game.ui.views.historic.HistoricViewState
import com.luisma.game.ui.views.stats.StatsViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun StatsSuccess(
    modifier: Modifier = Modifier,
    state: StatsViewState,
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        // title
        WText(
            text = stringResource(id = R.string.stats_title),
            wTextType = WTextType.T1,
            fontSize = WFontSize.k28
        )
        Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))
        // stats labels
        StatsLabels(
            modifier = Modifier.fillMaxWidth(),
            played = state.stats.playedGames,
            recordStreak = state.stats.recordStreak,
            percentWin = state.stats.winedPercentage,
            currentStreak = state.stats.currentStreak
        )
        Spacer(modifier = Modifier.padding(bottom = WSpacing.k40))
        // win distribution title
        WText(
            text = stringResource(id = R.string.stats_graph_title),
            wTextType = WTextType.T1,
            fontSize = WFontSize.k28
        )
        Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))
        // win distribution graphs
        StatsGraphs(
            distribution = state.stats.winDistribution
        )
    }
}


@Composable
internal fun StatsGraphs(
    distribution: ImmutableList<UserGameStatsWinDistribution>
) {
    distribution.forEachIndexed { idx, graph ->
        StatPercentBar(
            modifier = Modifier.padding(bottom = WSpacing.k5),
            idx = idx + 1,
            value = graph.value,
            percentage = graph.percentageWithMaxAsReference,
            highlight = graph.percentageWithMaxAsReference == 1f && graph.value != 0
        )
    }
}

@Preview
@Composable
private fun StatsSuccessPrev() {
    val winDistributions = persistentListOf(
        UserGameStatsWinDistribution(
            value = 20,
            percentageWithMaxAsReference = .5f
        ),
        UserGameStatsWinDistribution(
            value = 30,
            percentageWithMaxAsReference = .1f
        ),
        UserGameStatsWinDistribution(
            value = 0,
            percentageWithMaxAsReference = 1f
        )
    )
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color = WTheme.colors.background
        ) {
            StatsSuccess(
                state = StatsViewState.initial().copy(
                    stats = UserGamesStats.empty().copy(
                        winDistribution = winDistributions
                    )
                )
            )
        }
    }
}