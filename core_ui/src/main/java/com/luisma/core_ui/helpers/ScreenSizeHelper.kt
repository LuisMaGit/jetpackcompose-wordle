package com.luisma.core_ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ScreenSizeBreakPoints {
    Mobile,
    Tablet
}

@Composable
fun ScreenSizeHelper(
    content: @Composable (
        breakPoints: ScreenSizeBreakPoints,
        screenWidthDp: Dp
    ) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    when (configuration.screenWidthDp.dp) {
        in 0.dp..700.dp -> content(ScreenSizeBreakPoints.Mobile, screenWidthDp)
        else -> content(ScreenSizeBreakPoints.Tablet, screenWidthDp)
    }
}