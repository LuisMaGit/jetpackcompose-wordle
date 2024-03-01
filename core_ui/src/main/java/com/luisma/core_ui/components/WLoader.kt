package com.luisma.core_ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.theme.WThemeProvider

@Composable
fun WLoader(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wloader")
    val rotation by infiniteTransition.animateFloat(
        targetValue = 360f,
        initialValue = 0f,
        label = "wloader",
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    CharBox(
        modifier = modifier.graphicsLayer {
            rotationY = rotation
        },
        charState = CharBoxType.CharOk,
        char = 'P',
    )
}


@Preview
@Composable
fun WLoaderPrev() {
    WThemeProvider {
        WLoader()
    }
}