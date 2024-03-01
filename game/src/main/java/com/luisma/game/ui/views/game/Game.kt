package com.luisma.game.ui.views.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.luisma.core.models.BasicScreenState
import com.luisma.core_ui.components.WLoader
import com.luisma.game.ui.views.game.components.GameSuccess

@Composable
fun Game(
    state: GameState = GameState.default(),
    events: (event: GameEvents) -> Unit
) {
    when (state.screenState) {
        BasicScreenState.Success -> GameSuccess(
            state = state,
            events = events,
        )

        BasicScreenState.Loading -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            WLoader()
        }

        else -> Box {}
    }
}
