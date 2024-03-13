package com.luisma.game.ui.views.historic_filter

import com.luisma.core.models.BasicScreenState
import com.luisma.core.models.db.UserWordsPlayingStateContract
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.annotation.concurrent.Immutable

@Immutable
data class HistoricFilterViewState(
    val filters: ImmutableList<HistoricFilterSelection>,
    val initialised: Boolean,
    val screenState: BasicScreenState
) {
    companion object {
        fun initial(): HistoricFilterViewState {
            return HistoricFilterViewState(
                filters = persistentListOf(),
                initialised = false,
                screenState = BasicScreenState.Loading
            )
        }
    }
}

@Immutable
data class HistoricFilterSelection(
    val selected: Boolean,
    val state: UserWordsPlayingStateContract,
    val count: Int,
)