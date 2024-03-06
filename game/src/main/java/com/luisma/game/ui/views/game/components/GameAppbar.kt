package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.luisma.core_ui.R
import com.luisma.core_ui.components.WIconButton
import com.luisma.core_ui.theme.WSpacing

@Composable
internal fun GameAppbar(
    modifier: Modifier = Modifier,
    onTapStats: (() -> Unit?)?,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                WSpacing.k10
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        WIconButton(
            id = R.drawable.ic_charts,
            enabled = onTapStats != null
        ) {
            if (onTapStats != null) onTapStats()
        }
    }
}