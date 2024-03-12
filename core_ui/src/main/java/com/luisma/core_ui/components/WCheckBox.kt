package com.luisma.core_ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.R
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider

@Composable
fun WCheckBox(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String,
    onTap: () -> Unit,
    colorText: Color
) {
    Row(
        modifier = modifier.clickable { onTap() },
    ) {
        SvgImage(
            id = if (enabled) {
                R.drawable.ic_check_on
            } else {
                R.drawable.ic_check_off
            }
        )
        Spacer(modifier = Modifier.padding(end = WSpacing.k10))
        WText(
            text = text,
            color = colorText,
            wTextType = WTextType.T2
        )
    }
}

@Composable
@Preview
private fun WCheckBoxPrev() {
    WThemeProvider {
        WCheckBox(
            enabled = true,
            text = "Lorem ipsum",
            colorText = WTheme.colors.placeholderGreen,
            onTap = {}
        )
    }
}