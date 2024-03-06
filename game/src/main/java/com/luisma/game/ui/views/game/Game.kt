package com.luisma.game.ui.views.game

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.luisma.core.models.BasicScreenState
import com.luisma.game.ui.views.game.components.GameLoading
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

        BasicScreenState.Loading -> GameLoading()

        else -> Box {}
    }
}
