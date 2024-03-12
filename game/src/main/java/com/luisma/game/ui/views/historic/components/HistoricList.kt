package com.luisma.game.ui.views.historic.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.components.CharBoxDimensions
import com.luisma.core_ui.components.WLoader
import com.luisma.core_ui.helpers.ScreenSizeHelper
import com.luisma.core_ui.theme.WSpacing
import com.luisma.game.models.UserHistoricWord
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoricList(
    showLoader: Boolean,
    onTapBack: () -> Unit,
    onTapTile: (index: Int) -> Unit,
    triggerPagination: () -> Unit,
    onTapFilter: () -> Unit,
    isFilterApplied: Boolean,
    historic: ImmutableList<UserHistoricWord>
) {
    ScreenSizeHelper { screenSizeBreakPoints, _ ->
        val charBoxDimensions = CharBoxDimensions.charBoxDimensionsByScreenSize(
            screenSizeBreakPoints
        )
        HistoricWrapper(
            horizontalAlignment = Alignment.CenterHorizontally,
            onTapBack = onTapBack,
            onTapFilter = onTapFilter,
            isFilterApplied = isFilterApplied
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                stickyHeader {

                }
                // data
                items(
                    count = historic.count()
                ) { index ->
                    HistoricTile(
                        modifier = Modifier.padding(
                            top = WSpacing.k18,
                            bottom = if (index == historic.count() - 1) WSpacing.k18 else 0.dp
                        ),
                        index = index,
                        word = historic[index],
                        boxDimensions = charBoxDimensions,
                        onTap = onTapTile
                    )
                }

                // loader for last page
                if (showLoader) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = WSpacing.k18),
                            contentAlignment = Alignment.Center
                        ) {
                            LaunchedEffect(
                                key1 = Unit,
                                block = { triggerPagination() }
                            )
                            WLoader()
                        }
                    }
                }
            }
        }
    }
}