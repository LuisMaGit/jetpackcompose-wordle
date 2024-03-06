package com.luisma.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.R
import com.luisma.core_ui.theme.WBorderRadiusContract
import com.luisma.core_ui.theme.WColorContract
import com.luisma.core_ui.theme.WColors
import com.luisma.core_ui.theme.WThemeProvider

@Composable
fun WLogo(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(32.dp).background(
            color = WColorContract.placeholderGreen,
            shape = RoundedCornerShape(WBorderRadiusContract.k10)
        ),
        contentAlignment = Alignment.Center
    ) {
        WText(
            text = "P",
            wTextType = WTextType.T1,
            color = WColorContract.white
        )
    }
}

@Composable
@Preview
private fun WLogoPrev() {
    WThemeProvider {
        WLogo()
    }
}