package com.luisma.game.ui.views.historic_filter

import androidx.lifecycle.ViewModel
import com.luisma.core.models.db.UserWordsPlayingStateContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoricFilterViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HistoricFilterViewState.initial())
    val state = _state.asStateFlow()

    private lateinit var setFilterCallBack: (
        states: ImmutableList<UserWordsPlayingStateContract>
    ) -> Unit


    fun sendEvent(event: HistoricFilterEvents) {
        when (event) {
            is HistoricFilterEvents.InitHistoricFilter -> initHistoricFilter(
                states = event.filters,
                setFilter = event.setFilterCallBack
            )

            is HistoricFilterEvents.ToggleFilter -> toggleFilter(event.filterIdx)
            is HistoricFilterEvents.DisposeHistoricFilters -> dispose()
        }
    }


    private fun initHistoricFilter(
        states: ImmutableList<UserWordsPlayingStateContract>,
        setFilter: (
            states: ImmutableList<UserWordsPlayingStateContract>
        ) -> Unit
    ) {
        if (_state.value.initialised) {
            return
        }

        setFilterCallBack = setFilter
        val filters = mutableListOf<HistoricFilterSelection>()
        for (state in UserWordsPlayingStateContract.values()) {
            filters.add(
                HistoricFilterSelection(
                    selected = states.contains(state),
                    state = state
                )
            )
        }

        _state.update {
            it.copy(
                filters = filters.toImmutableList(),
                initialised = true
            )
        }
    }

    private fun toggleFilter(index: Int) {

        val mutable = _state.value.filters.toMutableList()
        if (mutable[index].selected && _state.value.filters.count { it.selected } == 1) {
            return
        }

        mutable[index] = mutable[index].copy(
            selected = !mutable[index].selected
        )

        val filtersOnly = mutable
            .filter { it.selected }
            .map { it.state }.toImmutableList()

        setFilterCallBack(filtersOnly)

        _state.update {
            it.copy(
                filters = mutable.toImmutableList(),
            )
        }
    }

    private fun dispose() {
        _state.update {
            it.copy(
                initialised = false
            )
        }
    }


}