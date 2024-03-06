package com.luisma.core_ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.luisma.core_ui.theme.WSpacing

@Composable
fun WIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    enabled: Boolean = true,
    onTap: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(enabled = enabled) { onTap() }
            .padding(WSpacing.k5),
        contentAlignment = Alignment.CenterEnd
    ) {
        SvgImage(id = id)
    }
}