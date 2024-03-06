package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.luisma.core_ui.R
import com.luisma.core_ui.components.WIconButton
import com.luisma.core_ui.components.WLogo
import com.luisma.core_ui.theme.WSpacing

@Composable
internal fun GameAppbar(
    modifier: Modifier = Modifier,
    onTapStats: (() -> Unit)? = null,
    onTapTutorial: (() -> Unit)? = null,
    onTapHistory: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                WSpacing.k10
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        WLogo()
        Spacer(modifier = Modifier.weight(1f))
        WIconButton(
            id = R.drawable.ic_reverse_clock,
            enabled = onTapHistory != null
        ) {
            if (onTapHistory != null) onTapHistory()
        }
        Spacer(modifier = Modifier.padding(end = WSpacing.k10))
        WIconButton(
            id = R.drawable.ic_charts,
            enabled = onTapStats != null
        ) {
            if (onTapStats != null) onTapStats()
        }
        Spacer(modifier = Modifier.padding(end = WSpacing.k10))
        WIconButton(
            id = R.drawable.ic_question,
            enabled = onTapTutorial != null
        ) {
            if (onTapTutorial != null) onTapTutorial()
        }
    }
}