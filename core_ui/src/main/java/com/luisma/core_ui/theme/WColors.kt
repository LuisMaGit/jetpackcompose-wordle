package com.luisma.core_ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
data class WColors(
    val background: Color,
    val inverseBackground: Color,
    val placeholderBorder: Color,
    val placeholderBorderBold: Color,
    val placeholderFill: Color,
    val keyDefault: Color,
    val placeholderGreen: Color = WColorContract.placeholderGreen,
    val placeholderOrange: Color = WColorContract.placeholderOrange,
) {
    companion object {
        fun light(): WColors =
            WColors(
                background = WColorContract.white,
                inverseBackground = WColorContract.black,
                placeholderBorder = WColorContract.placeholderBorderLight,
                placeholderBorderBold = WColorContract.placeholderBorderBoldLight,
                placeholderFill = WColorContract.placeholderFillLight,
                keyDefault = WColorContract.keyDefaultLight
            )

        fun dark(): WColors =
            WColors(
                background = WColorContract.black,
                inverseBackground = WColorContract.white,
                placeholderBorder = WColorContract.placeholderBorderDark,
                placeholderBorderBold = WColorContract.placeholderBorderBoldDark,
                placeholderFill = WColorContract.placeholderFillDark,
                keyDefault = WColorContract.keyDefaultDark
            )
    }
}

object WColorContract {
    val placeholderGreen = Color(0XFF02C39A)
    val placeholderOrange = Color(0xFFF5793A)
    val placeholderFillDark = Color(0XFF787C7E)
    val placeholderFillLight = Color(0XFF787C7E)
    val placeholderBorderBoldLight = Color(0XFF878A8C)
    val placeholderBorderBoldDark = Color(0XFFCED5DB)
    val placeholderBorderLight = Color(0XFFD2D6DA)
    val placeholderBorderDark = Color(0XFF565758)
    val keyDefaultLight = Color(0XFFD2D6DA)
    val keyDefaultDark = Color(0XFF818384)
    val white = Color(0XFFF8F9FA)
    val black = Color(0XFF1D1F25)
}

internal val LocalWColors = staticCompositionLocalOf { WColors.light() }