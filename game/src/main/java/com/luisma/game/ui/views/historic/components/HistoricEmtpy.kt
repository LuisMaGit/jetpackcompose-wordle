package com.luisma.game.ui.views.historic.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import com.luisma.core_ui.components.WText

@Composable
fun HistoricEmpty(
    onTapBack: () -> Unit,
    onTapFilter: (()-> Unit)?,
    text: String,
    isFilterApplied: Boolean,
) {
    HistoricWrapper(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        onTapBack = onTapBack,
        onTapFilter = onTapFilter,
        isFilterApplied = isFilterApplied
    ) {
        Spacer(modifier = Modifier.weight(1f))
        WText(text)
        Spacer(modifier = Modifier.weight(1f))
    }
}