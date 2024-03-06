package com.luisma.game.ui.views.stats

import com.luisma.core.models.BasicScreenState
import com.luisma.game.models.GameUserStats
import javax.annotation.concurrent.Immutable

@Immutable
data class StatsState(
    val screenState: BasicScreenState,
    val stats: GameUserStats,
    val initialised: Boolean
) {
    companion object {
        fun initial(): StatsState {
            return StatsState(
                screenState = BasicScreenState.Loading,
                stats = GameUserStats.empty(),
                initialised = false,
            )
        }
    }
}