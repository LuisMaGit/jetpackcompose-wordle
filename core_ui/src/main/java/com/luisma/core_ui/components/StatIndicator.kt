package com.luisma.core_ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.core_ui.theme.WFontSize

@Composable
fun StatIndicator(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {

    val colors = WTheme.colors
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WText(
            text = value,
            wTextType = WTextType.T1,
            fontSize = WFontSize.k32,
            color = colors.placeholderGreen
        )
        WText(
            text = title,
            wTextType = WTextType.T2,
            color = colors.placeholderBorderBold
        )
    }
}

@Preview
@Composable
private fun StatIndicatorPrev() {
    WThemeProvider {
        StatIndicator(
            title = "Jugados %",
            value = "12"
        )
    }
}