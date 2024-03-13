package com.luisma.game.ui.views.historic_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisma.core.models.BasicScreenState
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.game.models.UserHistoricFilterCount
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
class HistoricFilterViewModel @Inject constructor(
    private val userHistoricService: UserHistoricService
) : ViewModel() {

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
        viewModelScope.launch {
            val historicCount = userHistoricService.getFilterCount()
            val filters = mutableListOf<HistoricFilterSelection>()
            for (state in UserWordsPlayingStateContract.values()) {
                filters.add(
                    HistoricFilterSelection(
                        selected = states.contains(state),
                        state = state,
                        count = getCountState(
                            state = state,
                            count = historicCount
                        )
                    )
                )
            }

            _state.update {
                it.copy(
                    filters = filters.toImmutableList(),
                    initialised = true,
                    screenState = BasicScreenState.Success
                )
            }
        }
    }

    private fun getCountState(
        state: UserWordsPlayingStateContract,
        count: UserHistoricFilterCount
    ): Int {
        return when (state) {
            UserWordsPlayingStateContract.PLAYING -> count.playing
            UserWordsPlayingStateContract.LOSE -> count.lost
            UserWordsPlayingStateContract.SOLVED_IN_TIME -> count.solvedOnTime
            UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME -> count.solvedNotOnTime
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