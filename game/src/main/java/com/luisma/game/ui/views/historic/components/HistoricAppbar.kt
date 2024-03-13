package com.luisma.game.ui.views.historic.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.R
import com.luisma.core_ui.components.WIconButton
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.theme.WFontSize
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider

@Composable
internal fun HistoricAppbar(
    onTapBack: () -> Unit,
    onTapFilter: (() -> Unit)? = null,
    onTapRefresh: (() -> Unit)? = null,
    isFilterApplied: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                WSpacing.k10
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WIconButton(
            id = R.drawable.ic_back_arrow,
            onTap = { onTapBack() }
        )
        Spacer(modifier = Modifier.padding(end = WSpacing.k10))
        WText(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.historic_title),
            fontSize = WFontSize.k32
        )
        WIconButton(
            id = R.drawable.ic_refresh,
            enabled = onTapRefresh != null,
            onTap = { if (onTapRefresh != null) onTapRefresh() }
        )
        Spacer(modifier = Modifier.padding(end = WSpacing.k20))
        WIconButton(
            id = R.drawable.ic_filter,
            enabled = onTapFilter != null,
            selected = isFilterApplied,
            onTap = { if (onTapFilter != null) onTapFilter() }
        )
    }
}

@Preview
@Composable
private fun HistoricAppbarPreview() {
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color = WTheme.colors.background
        ) {
            HistoricAppbar(
                onTapBack = {},
                onTapFilter = {},
                isFilterApplied = true
            )
        }
    }
}