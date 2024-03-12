package com.luisma.game.ui.views.stats

import com.luisma.core.models.BasicScreenState
import com.luisma.game.models.UserGamesStats
import javax.annotation.concurrent.Immutable

@Immutable
data class StatsViewState(
    val screenState: BasicScreenState,
    val stats: UserGamesStats,
    val initialised: Boolean
) {
    companion object {
        fun initial(): StatsViewState {
            return StatsViewState(
                screenState = BasicScreenState.Loading,
                stats = UserGamesStats.empty(),
                initialised = false,
            )
        }
    }
}