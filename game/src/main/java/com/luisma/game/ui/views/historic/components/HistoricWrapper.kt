package com.luisma.game.ui.views.historic.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HistoricWrapper(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onTapBack: () -> Unit,
    onTapFilter: (() -> Unit)? = null,
    onTapRefresh: (() -> Unit)? = null,
    isFilterApplied: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        HistoricAppbar(
            onTapBack = onTapBack,
            onTapFilter = onTapFilter,
            onTapRefresh = onTapRefresh,
            isFilterApplied = isFilterApplied
        )
        content()
    }
}