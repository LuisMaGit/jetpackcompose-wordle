package com.luisma.game.ui.views.historic.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core_ui.R
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.models.GameDate
import com.luisma.game.models.ListCharsWithState
import com.luisma.game.models.UserHistoricWord
import com.luisma.game.models.WChar
import com.luisma.game.ui.views.historic.HistoricViewEvents
import com.luisma.game.ui.views.historic.HistoricViewState
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HistoricSuccess(
    state: HistoricViewState,
    events: (event: HistoricViewEvents) -> Unit
) {

    if (state.historic.isEmpty()) {
        HistoricEmpty(
            onTapBack = { events(HistoricViewEvents.GoBack) },
            onTapFilter = if (state.isTheFilterApplied) {
                { events(HistoricViewEvents.HandleFilter(showFilter = true)) }
            } else {
                null
            },
            text = if (state.isTheFilterApplied) {
                stringResource(id = R.string.historic_filter_empty)
            } else {
                stringResource(id = R.string.historic_empty)
            },
            isFilterApplied = state.isTheFilterApplied
        )
    } else {
        HistoricList(
            showLoader = !state.isLastPage,
            historic = state.historic,
            onTapBack = { events(HistoricViewEvents.GoBack) },
            triggerPagination = { events(HistoricViewEvents.OnLastItemCreation) },
            onTapFilter = { events(HistoricViewEvents.HandleFilter(showFilter = true)) },
            onTapTile = { index -> events(HistoricViewEvents.OnTapTile(index = index)) },
            isFilterApplied = state.isTheFilterApplied,
        )
    }

}

@Preview
@Composable
private fun HistoricSuccessPrev() {
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color = WTheme.colors.background
        ) {
            HistoricSuccess(
                state = HistoricViewState.initial().copy(
                    historic = persistentListOf(
                        UserHistoricWord(
                            lastChars = ListCharsWithState(
                                done = false,
                                chars = persistentListOf(
                                    WChar.boxEmpty(),
                                    WChar.boxEmpty(),
                                    WChar.boxEmpty(),
                                    WChar.boxEmpty(),
                                    WChar.boxEmpty(),
                                )
                            ),
                            state = UserWordsPlayingStateContract.PLAYING,
                            tryNumber = 3,
                            maxTries = 6,
                            date = GameDate.noTime(),
                            wordId = 0
                        )
                    )
                ),
                events = {}
            )
        }
    }
}