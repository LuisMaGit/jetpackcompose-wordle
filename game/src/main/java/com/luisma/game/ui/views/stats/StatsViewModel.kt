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
    private val _state = MutableStateFlow(StatsState.initial())
    val state = _state.asStateFlow()

    fun sendEvent(event: StatsEvents) {
        when (event) {
            is StatsEvents.InitStats -> getStats()
            is StatsEvents.DisposeStats -> _state.update { StatsState.initial() }
        }
    }

    private fun getStats() {
        if (!_state.value.initialised)
            viewModelScope.launch {
                val stats = userStatsService.getUserStats()
                _state.update {
                    StatsState(
                        screenState = BasicScreenState.Success,
                        stats = stats,
                        initialised = false
                    )
                }
            }
    }

}