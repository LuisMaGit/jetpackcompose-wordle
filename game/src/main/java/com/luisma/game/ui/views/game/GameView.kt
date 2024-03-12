package com.luisma.game.ui.views.game

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.luisma.core.models.BasicScreenState
import com.luisma.game.ui.views.game.components.GameFinished
import com.luisma.game.ui.views.game.components.GameLoading
import com.luisma.game.ui.views.game.components.GamePlay
import com.luisma.game.ui.views.stats.StatsViewBs
import com.luisma.game.ui.views.stats.StatsViewModel
import com.luisma.game.ui.views.tutorial.TutorialViewBs

@Composable
fun GameView(
    state: GameViewState = GameViewState.default(),
    events: (event: GameViewEvents) -> Unit,
) {
    when (state.screenState) {
        BasicScreenState.Success -> {
            if (state.gameFinished) {
                GameFinished(state = state, events = events)
            } else {
                GamePlay(state = state, events = events)
            }
        }

        BasicScreenState.Loading -> GameLoading(
            onTapBack = { events(GameViewEvents.GoBack) },
            viewType = state.gameType
        )

        else -> Box {}
    }
}

@Composable
fun GameViewBuilder(
) {
    val gameViewModel = hiltViewModel<GameViewModel>()
    val gameState by gameViewModel.state.collectAsState()
    // game
    GameView(
        state = gameState,
        events = gameViewModel::sendEvent,
    )
    // stats bs
    if (gameState.showStats) {
        val statsViewModel = hiltViewModel<StatsViewModel>()
        val statsState by statsViewModel.state.collectAsState()
        StatsViewBs(
            onDismiss = {
                gameViewModel.sendEvent(
                    GameViewEvents.HandleStats(showStats = false)
                )
            },
            state = statsState,
            events = { event -> statsViewModel.sendEvent(event) }
        )
    }
    // tutorial bs
    if (gameState.showTutorial)
        TutorialViewBs(
            onDismissRequest = {
                gameViewModel.sendEvent(
                    GameViewEvents.HandleTutorial(showTutorial = false)
                )
            }
        )
}
