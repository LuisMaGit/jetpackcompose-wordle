package com.luisma.game.ui.views.historic.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.components.WLoader
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider

@Composable
internal fun HistoricLoading(
    onTapBack: () -> Unit,
    isFilterApplied: Boolean,
) {
    HistoricWrapper(
        horizontalAlignment = Alignment.CenterHorizontally,
        onTapBack = onTapBack,
        isFilterApplied = isFilterApplied
    ) {
        Spacer(modifier = Modifier.weight(1f))
        WLoader()
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun HistoricLoadingPrev() {
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color = WTheme.colors.background
        ) {
            HistoricLoading(
                onTapBack = {},
                isFilterApplied = false
            )
        }
    }
}