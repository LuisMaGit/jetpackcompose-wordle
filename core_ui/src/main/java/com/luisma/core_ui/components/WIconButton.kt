package com.luisma.core_ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme

@Composable
fun WIconButton(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    enabled: Boolean = true,
    selected: Boolean = false,
    onTap: () -> Unit
) {

    Box(
        modifier = modifier
            .clickable(enabled = enabled) { onTap() }
            .padding(WSpacing.k5),
        contentAlignment = Alignment.CenterEnd
    ) {
        SvgImage(id = id)
        if (selected) {
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .background(
                        color = WTheme.colors.placeholderGreen,
                        shape = CircleShape
                    )
                    .size(10.dp)
            )
        }
    }

}