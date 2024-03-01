package com.luisma.core_ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.luisma.core_ui.R

object WType {
    val t1 = TextStyle(
        fontFamily = nunitoFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = WTypeSizeContract.k16,
    )

    val t2 = TextStyle(
        fontFamily = nunitoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = WTypeSizeContract.k16,
    )

    val t3 = TextStyle(
        fontFamily = nunitoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = WTypeSizeContract.k16,
    )
}

object WTypeSizeContract {
    val k12 = 12.sp
    val k16 = 16.sp
    val k20 = 20.sp
    val k32 = 32.sp
}

val nunitoFontFamily = FontFamily(
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold),
    Font(R.font.nunito_regular, FontWeight.Normal),
)

internal val LocalWTheme = staticCompositionLocalOf { WType }