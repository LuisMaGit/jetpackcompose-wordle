package com.luisma.game.ui.views.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.luisma.core.models.BasicScreenState
import com.luisma.core_ui.components.WBottomSheet
import com.luisma.core_ui.components.WLoader
import com.luisma.core_ui.theme.WScreenFractions
import com.luisma.game.ui.views.stats.components.StatsSuccess

@Composable
fun StatsView(
    state: StatsViewState,
    events: (event: StatsViewEvents) -> Unit
) {
    DisposableEffect(key1 = Unit) {
        events(StatsViewEvents.InitStats)
        onDispose {
            events(StatsViewEvents.DisposeStats)
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (state.screenState) {
            BasicScreenState.Success -> StatsSuccess(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(WScreenFractions.k90)
                    .align(Alignment.TopCenter)
            )

            BasicScreenState.Loading -> WLoader(
                modifier = Modifier.align(Alignment.Center)
            )

            else -> Box {}
        }
    }
}

@Composable
fun StatsViewBs(
    state: StatsViewState,
    events: (event: StatsViewEvents) -> Unit,
    onDismiss: () -> Unit
) {
    WBottomSheet(
        onDismissRequest = { onDismiss() },
        show = true
    ) {
        StatsView(
            state = state,
            events = events,
        )
    }
}

