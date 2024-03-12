package com.luisma.game.ui.views.historic

import com.luisma.core.models.db.UserWordsPlayingStateContract
import kotlinx.collections.immutable.ImmutableList

sealed class HistoricViewEvents {
    object OnLastItemCreation : HistoricViewEvents()

    object GoBack : HistoricViewEvents()

    data class HandleFilter(val showFilter: Boolean) : HistoricViewEvents()

    data class SetFilter(
        val filter: ImmutableList<UserWordsPlayingStateContract>
    ) : HistoricViewEvents()

    data class OnTapTile(
        val index: Int
    ) : HistoricViewEvents()
}