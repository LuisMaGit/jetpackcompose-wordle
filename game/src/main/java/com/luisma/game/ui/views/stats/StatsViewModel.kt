package com.luisma.game.ui.views.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luisma.core.models.BasicScreenState
import com.luisma.game.services.UserStatsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val userStatsService: UserStatsService,
) : ViewModel() {
    private val _state = MutableStateFlow(StatsViewState.initial())
    val state = _state.asStateFlow()

    fun sendEvent(event: StatsViewEvents) {
        when (event) {
            is StatsViewEvents.InitStats -> getStats()
            is StatsViewEvents.DisposeStats -> _state.update { StatsViewState.initial() }
        }
    }

    private fun getStats() {
        if (!_state.value.initialised)
            viewModelScope.launch {
                val stats = userStatsService.getUserStats()
                _state.update {
                    StatsViewState(
                        screenState = BasicScreenState.Success,
                        stats = stats,
                        initialised = false
                    )
                }
            }
    }

}