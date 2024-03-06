package com.luisma.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.core_ui.theme.WType

enum class WTextType {
    T1,
    T2,
    T3
}

@Composable
fun WText(
    text: String,
    modifier: Modifier = Modifier,
    wTextType: WTextType = WTextType.T1,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    fontSize: TextUnit? = null,
    color: Color? = null
) {

    fun getTextStyle(
        wColor: Color,
        wType: WType,
    ): TextStyle {
        val type = when (wTextType) {
            WTextType.T1 -> wType.t1
            WTextType.T2 -> wType.t2
            WTextType.T3 -> wType.t3
        }
        return type.copy(
            color = color ?: wColor,
            fontSize = fontSize ?: type.fontSize
        )
    }

    Text(
        text = text,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        style = getTextStyle(
            wColor = WTheme.colors.inverseBackground,
            wType = WTheme.typography,
        ),
    )
}

@Preview
@Composable
private fun WTextPreview() {
    WThemeProvider {
        Column(
            modifier = Modifier.background(color = Color.White)
        ) {
            WText(
                text = "T1 Lorem ipsum",
                wTextType = WTextType.T1
            )
            WText(
                text = "T2 Lorem ipsum",
                wTextType = WTextType.T2
            )
            WText(
                text = "T3 Lorem ipsum",
                wTextType = WTextType.T3
            )
        }
    }
}

@Preview
@Composable
private fun WTextDarkPreview() {
    WThemeProvider(
        darkTheme = true
    ) {
        Column {
            WText(
                text = "T1 Lorem ipsum",
                wTextType = WTextType.T1
            )
            WText(
                text = "T2 Lorem ipsum",
                wTextType = WTextType.T2
            )
            WText(
                text = "T3 Lorem ipsum",
                wTextType = WTextType.T3
            )
        }
    }
}