package com.luisma.game.ui.views.historic

import com.luisma.core.models.BasicScreenState
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.game.models.UserHistoricWord
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HistoricViewState(
    val filter: List<UserWordsPlayingStateContract>,
    val screenState: BasicScreenState,
    val historic: ImmutableList<UserHistoricWord>,
    val isLastPage: Boolean,
    val page: Int,
    val loadingNextPage: Boolean,
    val showFilter: Boolean,
    val isTheFilterApplied : Boolean
) {
    companion object {
        fun initial(): HistoricViewState {
            return HistoricViewState(
                filter = UserWordsPlayingStateContract.values().toList(),
                historic = persistentListOf(),
                isLastPage = true,
                screenState = BasicScreenState.Loading,
                page = 0,
                loadingNextPage = false,
                showFilter = false,
                isTheFilterApplied = false
            )
        }
    }

}