package com.luisma.game.ui.views.game.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.components.CharBoxDimensions
import com.luisma.game.models.LettersRows
import com.luisma.game.models.WCharRowAnimationState

@Composable
internal fun GameGrid(
    modifier: Modifier = Modifier,
    gridData: LettersRows,
    boxDimension: CharBoxDimensions,
    horizontalRowAnimation: WCharRowAnimationState,
    currentlyPlayingRowIdx: Int,
    onDismissHorizontalAnimation: () -> Unit,
    onDismissScaleAnimation: ((rowIdx: Int, charIdx: Int) -> Unit),
    onDismissRowAnimation: ((rowIdx: Int) -> Unit),
) {

    val translationXAnimation = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = horizontalRowAnimation) {
        if (horizontalRowAnimation != WCharRowAnimationState.Still) {
            val result = translationXAnimation.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 250
                    -20f at 50
                    20f at 100
                    -20f at 150
                    20f at 200
                },
            )
            if (result.endReason == AnimationEndReason.Finished) {
                onDismissHorizontalAnimation()
            }
        }
    }

    fun verticalOffset(index: Int): Dp {
        if (index == 0) {
            return 0.dp
        }
        return (boxDimension.size + GameGridRowConstants.spacing) * index
    }

    fun buildModifier(index: Int): Modifier {
        return if (index == currentlyPlayingRowIdx) {
            Modifier.graphicsLayer {
                translationX = translationXAnimation.value
            }
        } else {
            Modifier
        }
    }

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier,
    ) {
        for ((index, entry) in gridData) {
            GameGridRow(
                modifier = Modifier
                    .then(buildModifier(index)),
                chars = entry.chars,
                boxDimension = boxDimension,
                verticalOffset = verticalOffset(index),
                rowIdx = index,
                onDismissScaleAnimation = onDismissScaleAnimation,
                onDismissRowAnimation = onDismissRowAnimation,
            )
        }
    }
}
