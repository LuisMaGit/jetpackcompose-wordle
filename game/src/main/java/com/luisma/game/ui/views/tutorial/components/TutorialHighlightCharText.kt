package com.luisma.game.ui.views.tutorial.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.components.WTextType
import com.luisma.core_ui.theme.WSpacing

@Composable
fun TutorialHighlightCharText(
    char: String,
    text: String
) {
    Row {
        WText(text = char)
        Spacer(modifier = Modifier.padding(end = WSpacing.k5))
        WText(
            text = text,
            wTextType = WTextType.T3
        )
    }
}