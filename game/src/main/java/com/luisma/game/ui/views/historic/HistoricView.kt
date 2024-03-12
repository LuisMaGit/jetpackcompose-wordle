package com.luisma.game.ui.views.historic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.luisma.core.models.BasicScreenState
import com.luisma.game.ui.views.historic.components.HistoricLoading
import com.luisma.game.ui.views.historic.components.HistoricSuccess
import com.luisma.game.ui.views.historic_filter.HistoricFilterViewBs
import com.luisma.game.ui.views.historic_filter.HistoricFilterViewModel
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HistoricView(
    state: HistoricViewState,
    events: (event: HistoricViewEvents) -> Unit
) {

    when (state.screenState) {
        BasicScreenState.Loading -> HistoricLoading(
            onTapBack = {},
            isFilterApplied = state.isTheFilterApplied,
        )

        BasicScreenState.Success -> HistoricSuccess(
            state = state,
            events = events
        )
    }
}


@Composable
fun HistoricViewBuilder() {
    val historicViewModel = hiltViewModel<HistoricViewModel>()
    val historicState by historicViewModel.state.collectAsState()
    // historic
    HistoricView(
        state = historicState,
        events = historicViewModel::sendEvent
    )
    // historic bs
    if (historicState.showFilter) {
        val historicFilterViewModel = hiltViewModel<HistoricFilterViewModel>()
        val historicFilterState by historicFilterViewModel.state.collectAsState()
        HistoricFilterViewBs(
            state = historicFilterState,
            inputFilter = historicState.filter.toImmutableList(),
            events = historicFilterViewModel::sendEvent,
            onDismissRequest = {
                historicViewModel.sendEvent(
                    HistoricViewEvents.HandleFilter(showFilter = false)
                )
            },
            onSet = { filter ->
                historicViewModel.sendEvent(
                    HistoricViewEvents.SetFilter(filter)
                )
            },
        )
    }
}
