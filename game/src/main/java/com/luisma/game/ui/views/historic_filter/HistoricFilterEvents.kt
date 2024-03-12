package com.luisma.game.ui.views.historic_filter

import com.luisma.core.models.db.UserWordsPlayingStateContract
import kotlinx.collections.immutable.ImmutableList

sealed class HistoricFilterEvents {
    data class InitHistoricFilter(
        val filters: ImmutableList<UserWordsPlayingStateContract>,
        val setFilterCallBack: (
            states: ImmutableList<UserWordsPlayingStateContract>
        ) -> Unit
    ) : HistoricFilterEvents()

    data class ToggleFilter(val filterIdx: Int) : HistoricFilterEvents()

    object DisposeHistoricFilters : HistoricFilterEvents()

}