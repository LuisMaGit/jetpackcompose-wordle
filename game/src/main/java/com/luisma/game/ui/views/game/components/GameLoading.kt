package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.luisma.core_ui.components.WLoader

@Composable
internal fun GameLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        GameAppbar()
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            WLoader()
        }
    }
}