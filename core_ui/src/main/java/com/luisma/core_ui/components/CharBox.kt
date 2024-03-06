package com.luisma.core_ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.helpers.ScreenSizeBreakPoints
import com.luisma.core_ui.helpers.fontSizeNonScaledSp
import com.luisma.core_ui.theme.WBorderRadiusContract
import com.luisma.core_ui.theme.WBorderWidthContract
import com.luisma.core_ui.theme.WColorContract
import com.luisma.core_ui.theme.WColors
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider

enum class CharBoxType {
    Empty,
    Char,
    CharMissing,
    CharMisplaced,
    CharOk,
}

enum class CharBoxAnimationType {
    NoAnimation,
    Scale,
    Rotate,
    Jump,
}

@Stable
data class CharBoxDimensions(
    val size: Dp,
    val cornerRadius: Dp,
    val fontSize: Int
) {
    companion object {
        fun small(): CharBoxDimensions {
            return CharBoxDimensions(
                size = 50.dp,
                cornerRadius = WBorderRadiusContract.k10,
                fontSize = 32
            )
        }

        fun big(): CharBoxDimensions {
            return CharBoxDimensions(
                size = 80.dp,
                cornerRadius = WBorderRadiusContract.k16,
                fontSize = 54
            )
        }

        fun charBoxDimensionsByScreenSize(
            screenSizeBreakPoints: ScreenSizeBreakPoints
        ): CharBoxDimensions {
            return when (screenSizeBreakPoints) {
                ScreenSizeBreakPoints.Mobile -> small()
                ScreenSizeBreakPoints.Tablet -> big()
            }
        }
    }
}


@Composable
fun CharBox(
    modifier: Modifier = Modifier,
    charState: CharBoxType,
    charAnimation: CharBoxAnimationType = CharBoxAnimationType.NoAnimation,
    dimensions: CharBoxDimensions = CharBoxDimensions.small(),
    char: Char? = null,
    charIdx: Int = 0,
    rowIdx: Int = 0,
    lastCharIdx: Int = 0,
    onDismissScaleAnimation: ((rowIdx: Int, charIdx: Int) -> Unit)? = null,
    onDismissRowAnimation: ((rowIdx: Int) -> Unit)? = null,
) {

    val isLastInRow = lastCharIdx == charIdx
    val triggerScale = charAnimation == CharBoxAnimationType.Scale

    // appear animation
    val scaleAnimation = remember {
        Animatable(1f)
    }


    // reveal animation
    val rotationXAnimation = remember {
        Animatable(0f)
    }

    // jump animation
    val translationYAnimation = remember {
        Animatable(0f)
    }

    val rotateAnimationCondition = (charAnimation == CharBoxAnimationType.Rotate
            || charAnimation == CharBoxAnimationType.Jump)
            && (charState == CharBoxType.CharOk
            || charState == CharBoxType.CharMisplaced
            || charState == CharBoxType.CharMissing)

    val jumpAnimationCondition = rotateAnimationCondition
            && charAnimation == CharBoxAnimationType.Jump

    LaunchedEffect(key1 = charAnimation) {
        // appear animation
        if (triggerScale) {
            val result = scaleAnimation.animateTo(
                targetValue = 1f,
                animationSpec = keyframes {
                    durationMillis = 100
                    1.05f at 50 using FastOutSlowInEasing
                },
            )
            if (result.endReason == AnimationEndReason.Finished
                && onDismissScaleAnimation != null
            ) {
                onDismissScaleAnimation(rowIdx, charIdx)
            }
        }


        // reveal animation and
        if (rotateAnimationCondition) {
            val result = rotationXAnimation.animateTo(
                targetValue = 180f,
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = charIdx * 250,
                    easing = EaseOut
                )
            )
            if (isLastInRow
                && result.endReason == AnimationEndReason.Finished
                && onDismissRowAnimation != null
                && !jumpAnimationCondition
            ) {
                onDismissRowAnimation(rowIdx)
            }
        }

        //success animation
        if (jumpAnimationCondition) {
            val result = translationYAnimation.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    delayMillis = 300 * lastCharIdx - 300
                    durationMillis = 600
                    -100f at 150
                    0f at 300
                    -100f at 450
                },
            )
            if (isLastInRow
                && result.endReason == AnimationEndReason.Finished
                && onDismissRowAnimation != null
            ) {
                onDismissRowAnimation(rowIdx)
            }
        }
    }

    fun buildStyleModifier(
        colors: WColors,
        type: CharBoxType = charState
    ): Modifier {
        val shape = RoundedCornerShape(dimensions.cornerRadius)
        return when (type) {
            CharBoxType.Empty ->
                Modifier.border(
                    width = WBorderWidthContract.k2,
                    color = colors.placeholderBorder,
                    shape = shape
                )

            CharBoxType.Char -> Modifier.border(
                width = WBorderWidthContract.k2,
                color = colors.placeholderBorderBold,
                shape = shape
            )

            CharBoxType.CharMissing -> Modifier
                .background(
                    color = colors.placeholderFill,
                    shape = shape
                )

            CharBoxType.CharMisplaced -> Modifier
                .background(
                    color = colors.placeholderOrange,
                    shape = shape
                )

            CharBoxType.CharOk -> Modifier
                .background(
                    color = colors.placeholderGreen,
                    shape = shape
                )
        }
    }

    @Composable
    fun textComponent(charBoxType: CharBoxType = charState) {
        WText(
            text = if (charBoxType == CharBoxType.Empty) "" else char.toString(),
            fontSize = dimensions.fontSize.fontSizeNonScaledSp,
            color = if (charBoxType != CharBoxType.Char) WColorContract.white else null
        )
    }

    @Composable
    fun revealComponent() {
        Box(
            modifier = modifier
                .size(dimensions.size)
                .graphicsLayer {
                    rotationX = if (rotateAnimationCondition) {
                        rotationXAnimation.value
                    } else {
                        0f
                    }
                    translationY = if (jumpAnimationCondition) {
                        translationYAnimation.value
                    } else {
                        0f
                    }
                },
        ) {
            if (rotationXAnimation.value <= 90f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            buildStyleModifier(
                                colors = WTheme.colors,
                                type = CharBoxType.Char
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    textComponent(charBoxType = CharBoxType.Char)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            buildStyleModifier(
                                colors = WTheme.colors,
                            )
                        )
                        .graphicsLayer {
                            rotationX = 180f
                        },
                    contentAlignment = Alignment.Center
                ) {
                    textComponent()
                }
            }
        }
    }

    if (rotateAnimationCondition) {
        revealComponent()
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .graphicsLayer {
                    scaleX = if (triggerScale) scaleAnimation.value else 1f
                    scaleY = if (triggerScale) scaleAnimation.value else 1f
                }
                .size(
                    if (triggerScale) {
                        dimensions.size * scaleAnimation.value
                    } else {
                        dimensions.size
                    }
                )
                .then(
                    buildStyleModifier(
                        colors = WTheme.colors,
                    )
                ),
        ) {
            textComponent()
        }
    }
}


@Preview
@Composable
private fun PreviewCharBox() {
    WThemeProvider {
        Row {
            //small
            Column {
                CharBox(
                    charState = CharBoxType.Empty,
                )
                CharBox(
                    charState = CharBoxType.Char,
                    char = 'T',
                )
                CharBox(
                    charState = CharBoxType.CharMissing,
                    char = 'T',
                )
                CharBox(
                    charState = CharBoxType.CharMisplaced,
                    char = 'T',
                )
                CharBox(
                    charState = CharBoxType.CharOk,
                    char = 'T',
                )
            }
            //big
            Column {
                CharBox(
                    charState = CharBoxType.Empty,
                    dimensions = CharBoxDimensions.big()
                )
                CharBox(
                    charState = CharBoxType.Char,
                    char = 'T',
                    dimensions = CharBoxDimensions.big()
                )
                CharBox(
                    charState = CharBoxType.CharMissing,
                    char = 'T',
                    dimensions = CharBoxDimensions.big()
                )
                CharBox(
                    charState = CharBoxType.CharMisplaced,
                    char = 'T',
                    dimensions = CharBoxDimensions.big()
                )
                CharBox(
                    charState = CharBoxType.CharOk,
                    char = 'T',
                    dimensions = CharBoxDimensions.big()
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCharBoxDark() {
    WThemeProvider(
        darkTheme = true
    ) {
        Column {
            CharBox(
                charState = CharBoxType.Empty,
            )
            CharBox(
                charState = CharBoxType.Char,
                char = 'T',
            )
            CharBox(
                charState = CharBoxType.CharMissing,
                char = 'T',
            )
            CharBox(
                charState = CharBoxType.CharMisplaced,
                char = 'T',
            )
            CharBox(
                charState = CharBoxType.CharOk,
                char = 'T',
            )
        }
    }
}