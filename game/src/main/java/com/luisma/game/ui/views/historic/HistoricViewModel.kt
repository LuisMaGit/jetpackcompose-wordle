package com.luisma.game.ui.views.historic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisma.core.models.BasicScreenState
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core.services.RoutePayload
import com.luisma.core.services.RouterService
import com.luisma.core.services.Routes
import com.luisma.game.services.UserHistoricService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoricViewModel @Inject constructor(
    private val userHistoricService: UserHistoricService,
    private val routerService: RouterService
) : ViewModel() {

    private val _state = MutableStateFlow(HistoricViewState.initial())
    val state = _state.asStateFlow()

    init {
        getHistoricData()
    }

    fun sendEvent(events: HistoricViewEvents) {
        when (events) {
            is HistoricViewEvents.OnLastItemCreation -> onLastItemCreation()
            is HistoricViewEvents.GoBack -> goBack()
            is HistoricViewEvents.HandleFilter -> handleFilter(events.showFilter)
            is HistoricViewEvents.SetFilter -> setFilter(events.filter)
            is HistoricViewEvents.OnTapTile -> onTapTile(events.index)
        }
    }

    private fun getHistoricData() {
        viewModelScope.launch {
            val historicResp = userHistoricService.getHistoricWords(
                page = _state.value.page,
                filter = _state.value.filter
            )
            _state.update {
                it.copy(
                    historic = historicResp.data,
                    isLastPage = historicResp.isLastPage,
                    screenState = BasicScreenState.Success,
                    isTheFilterApplied = _state.value.filter.count() !=
                            UserWordsPlayingStateContract.values().count()
                )
            }
        }
    }

    private fun onLastItemCreation() {
        if (_state.value.isLastPage || _state.value.loadingNextPage) {
            return
        }

        _state.update {
            it.copy(
                loadingNextPage = true,
                page = _state.value.page + 1
            )
        }

        viewModelScope.launch {
            val historicResp = userHistoricService.getHistoricWords(
                page = _state.value.page,
                filter = _state.value.filter
            )

            _state.update {
                it.copy(
                    historic = (_state.value.historic + historicResp.data).toImmutableList(),
                    isLastPage = historicResp.isLastPage,
                    loadingNextPage = false
                )
            }
        }

    }

    private fun goBack() {
        viewModelScope.launch {
            routerService.goBack()
        }
    }

    private fun handleFilter(show: Boolean) {
        _state.update {
            it.copy(showFilter = show)
        }
    }

    private fun setFilter(
        filter: ImmutableList<UserWordsPlayingStateContract>
    ) {
        _state.update {
            HistoricViewState.initial().copy(
                showFilter = _state.value.showFilter,
                isTheFilterApplied = _state.value.isTheFilterApplied,
                filter = filter
            )
        }
        getHistoricData()
    }

    private fun onTapTile(index: Int) {
        viewModelScope.launch {
            routerService.goTo(
                RoutePayload(
                    route = Routes.GameHistoric,
                    payload = _state.value.historic[index].wordId.toString()
                ),
            )
        }
    }
}














