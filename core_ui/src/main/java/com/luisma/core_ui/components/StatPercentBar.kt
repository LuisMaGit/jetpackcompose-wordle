package com.luisma.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.helpers.fontSizeNonScaledSp
import com.luisma.core_ui.theme.WBorderRadiusContract
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider

@Composable
fun StatPercentBar(
    modifier: Modifier = Modifier,
    idx: Int,
    value: Int,
    percentage: Float,
    highlight: Boolean,
) {

    val colors = WTheme.colors
    val highlightColor = if (highlight) {
        colors.placeholderGreen
    } else {
        colors.placeholderBorderBold
    }
    val valueStr = if (percentage != 0f) "\t$value\t" else value.toString()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WText(
            text = "$idx",
            wTextType = WTextType.T2,
            color = colors.placeholderBorderBold
        )
        Spacer(modifier = Modifier.padding(end = WSpacing.k10))
        Box(
            modifier = Modifier
                .width(220.dp * percentage)
                .height(25.dp)
                .background(
                    color = highlightColor,
                    shape = RoundedCornerShape(size = WBorderRadiusContract.k10)
                )
        )
        WText(
            text = valueStr,
            color = highlightColor,
            wTextType = WTextType.T2,
            fontSize = 16.fontSizeNonScaledSp
        )
    }
}


@Preview
@Composable
private fun StatPercentBarPrev() {
    WThemeProvider {
        StatPercentBar(
            idx = 1,
            value = 999,
            percentage = 1f,
            highlight = false
        )
    }
}