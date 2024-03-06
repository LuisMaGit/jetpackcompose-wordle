package com.luisma.core_ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun SvgImage(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    tint: Color? = null,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = id),
        contentDescription = "",
        tint = tint ?: LocalContentColor.current
    )
}