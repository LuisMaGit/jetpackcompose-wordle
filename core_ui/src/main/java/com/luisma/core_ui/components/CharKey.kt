package com.luisma.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.helpers.ScreenSizeBreakPoints
import com.luisma.core_ui.helpers.fontSizeNonScaledSp
import com.luisma.core_ui.theme.WBorderRadiusContract
import com.luisma.core_ui.theme.WColorContract
import com.luisma.core_ui.theme.WColors
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider

enum class CharKeyType {
    Empty,
    CharMissing,
    CharMisplaced,
    CharOk,
}

@Stable
data class CharKeyDimensions(
    val width: Dp,
    val height: Dp,
    val fontSize: Int,
) {
    companion object {
        fun small(): CharKeyDimensions {
            return CharKeyDimensions(
                width = 30.dp,
                height = 58.dp,
                fontSize = 16
            )
        }

        fun big(): CharKeyDimensions {
            return CharKeyDimensions(
                width = 60.dp,
                height = 58.dp,
                fontSize = 24
            )
        }

        fun keyDimensionsByScreenSize(
            screenSizeBreakPoints: ScreenSizeBreakPoints
        ): CharKeyDimensions {
            return when (screenSizeBreakPoints) {
                ScreenSizeBreakPoints.Mobile -> small()
                ScreenSizeBreakPoints.Tablet -> big()
            }
        }
    }
}

@Composable
fun CharKey(
    modifier: Modifier = Modifier,
    charKeyType: CharKeyType,
    char: Char,
    dimensions: CharKeyDimensions = CharKeyDimensions.small(),
    enabled: Boolean,
    onTapChar: (char: Char) -> Unit
) {

    fun getBackgroundColor(colors: WColors): Color {
        return when (charKeyType) {
            CharKeyType.Empty -> colors.keyDefault
            CharKeyType.CharMissing -> colors.placeholderFill
            CharKeyType.CharMisplaced -> colors.placeholderOrange
            CharKeyType.CharOk -> colors.placeholderGreen
        }
    }

    fun getTextColor(): Color? {
        return if (charKeyType != CharKeyType.Empty) {
            return WColorContract.white
        } else {
            null
        }
    }


    Box(
        modifier = modifier
            .size(
                width = dimensions.width,
                height = dimensions.height
            )
            .clip(
                shape = RoundedCornerShape(WBorderRadiusContract.k6)
            )
            .background(
                color = getBackgroundColor(WTheme.colors),
            )
            .clickable(enabled = enabled) { onTapChar(char) },
        contentAlignment = Alignment.Center,
    ) {
        WText(
            text = char.toString(),
            wTextType = WTextType.T2,
            color = getTextColor(),
            fontSize = dimensions.fontSize.fontSizeNonScaledSp,
        )
    }
}

@Preview
@Composable
private fun CharKeyPreview() {
    WThemeProvider {
        Row {
            //small
            Column {
                CharKey(
                    charKeyType = CharKeyType.Empty,
                    char = 'T',
                    onTapChar = {},
                    enabled = true,
                )
                CharKey(
                    charKeyType = CharKeyType.CharMissing,
                    char = 'T',
                    onTapChar = {},
                    enabled = false,
                )
                CharKey(
                    charKeyType = CharKeyType.CharMisplaced,
                    char = 'T',
                    onTapChar = {},
                    enabled = false,
                )
                CharKey(
                    charKeyType = CharKeyType.CharOk,
                    char = 'T',
                    onTapChar = {},
                    enabled = false,
                )
            }
            //big
            Column {
                CharKey(
                    charKeyType = CharKeyType.Empty,
                    char = 'T',
                    dimensions = CharKeyDimensions.big(),
                    onTapChar = {},
                    enabled = false,
                )
                CharKey(
                    charKeyType = CharKeyType.CharMissing,
                    char = 'T',
                    dimensions = CharKeyDimensions.big(),
                    onTapChar = {},
                    enabled = false,
                )
                CharKey(
                    charKeyType = CharKeyType.CharMisplaced,
                    char = 'T',
                    dimensions = CharKeyDimensions.big(),
                    onTapChar = {},
                    enabled = false,
                )
                CharKey(
                    charKeyType = CharKeyType.CharOk,
                    char = 'T',
                    dimensions = CharKeyDimensions.big(),
                    onTapChar = {},
                    enabled = false,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CharKeyDarkPreview() {
    WThemeProvider(
        darkTheme = true
    ) {
        Column {
            CharKey(
                charKeyType = CharKeyType.Empty,
                char = 'T',
                onTapChar = {},
                enabled = false,
            )
            CharKey(
                charKeyType = CharKeyType.CharMissing,
                char = 'T',
                onTapChar = {},
                enabled = false,
            )
            CharKey(
                charKeyType = CharKeyType.CharMisplaced,
                char = 'T',
                onTapChar = {},
                enabled = false,
            )
            CharKey(
                charKeyType = CharKeyType.CharOk,
                char = 'T',
                onTapChar = {},
                enabled = false,
            )
        }
    }
}