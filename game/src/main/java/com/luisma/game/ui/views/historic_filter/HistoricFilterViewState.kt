package com.luisma.game.ui.views.historic_filter

import com.luisma.core.models.db.UserWordsPlayingStateContract
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import javax.annotation.concurrent.Immutable

@Immutable
data class HistoricFilterViewState(
    val filters: ImmutableList<HistoricFilterSelection>,
    val initialised: Boolean,
) {
    companion object {
        fun initial(): HistoricFilterViewState {
            return HistoricFilterViewState(
                filters = persistentListOf(),
                initialised = false
            )
        }
    }
}

@Immutable
data class HistoricFilterSelection(
    val selected: Boolean,
    val state: UserWordsPlayingStateContract
)