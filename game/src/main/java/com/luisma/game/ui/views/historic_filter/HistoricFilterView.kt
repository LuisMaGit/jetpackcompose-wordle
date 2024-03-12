package com.luisma.game.ui.views.historic_filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core_ui.R
import com.luisma.core_ui.components.WBottomSheet
import com.luisma.core_ui.components.WCheckBox
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.components.WTextType
import com.luisma.core_ui.theme.WFontSize
import com.luisma.core_ui.theme.WScreenFractions
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.ui.utils.gameStateTextColorUIMapper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


@Composable
fun HistoricFilterView(
    state: HistoricFilterViewState,
    inputFilter: ImmutableList<UserWordsPlayingStateContract>,
    events: (event: HistoricFilterEvents) -> Unit,
    onSet: (filter: ImmutableList<UserWordsPlayingStateContract>) -> Unit
) {

    val scrollState = rememberScrollState()

    DisposableEffect(
        key1 = Unit,
        effect = {
            events(
                HistoricFilterEvents.InitHistoricFilter(
                    filters = inputFilter,
                    setFilterCallBack = onSet
                )
            )
            onDispose {
                events(HistoricFilterEvents.DisposeHistoricFilters)
            }
        }
    )

    fun textStateMapper(state: UserWordsPlayingStateContract): Int {
        return when (state) {
            UserWordsPlayingStateContract.PLAYING -> R.string.historic_tile_playing
            UserWordsPlayingStateContract.LOSE -> R.string.historic_filter_lose
            UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME -> R.string.historic_tile_not_on_time
            UserWordsPlayingStateContract.SOLVED_IN_TIME -> R.string.historic_tile_on_time
        }
    }


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxWidth(WScreenFractions.k90)
                .align(Alignment.TopCenter)
        ) {
            WText(
                text = stringResource(id = R.string.historic_filter_title),
                wTextType = WTextType.T1,
                fontSize = WFontSize.k32
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))
            state.filters.forEachIndexed { index, filter ->
                WCheckBox(
                    modifier = Modifier.padding(bottom = WSpacing.k18),
                    enabled = filter.selected,
                    text = stringResource(id = textStateMapper(filter.state)),
                    onTap = {
                        events(HistoricFilterEvents.ToggleFilter(index))
                    },
                    colorText = gameStateTextColorUIMapper(
                        state = filter.state,
                        wColors = WTheme.colors
                    )
                )
            }
        }
    }
}


@Composable
fun HistoricFilterViewBs(
    state: HistoricFilterViewState,
    inputFilter: ImmutableList<UserWordsPlayingStateContract>,
    events: (event: HistoricFilterEvents) -> Unit,
    onDismissRequest: () -> Unit,
    onSet: (filter: ImmutableList<UserWordsPlayingStateContract>) -> Unit
) {
    WBottomSheet(
        show = true,
        onDismissRequest = onDismissRequest
    ) {
        HistoricFilterView(
            state = state,
            inputFilter = inputFilter,
            events = events,
            onSet = onSet,
        )
    }
}


@Preview
@Composable
fun HistoricFilterViewPrev() {
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color =  WTheme.colors.background
        ) {
            val filters = mutableListOf<HistoricFilterSelection>()
            for (state in UserWordsPlayingStateContract.values()) {
                filters.add(
                    HistoricFilterSelection(
                        selected = true,
                        state = state
                    )
                )
            }
            HistoricFilterView(
                state = HistoricFilterViewState.initial().copy(
                    filters = filters.toImmutableList()
                ),
                events = {},
                inputFilter = persistentListOf(),
                onSet = {}
            )
        }
    }
}