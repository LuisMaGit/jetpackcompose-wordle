package com.luisma.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.ui.views.game.Game
import com.luisma.game.ui.views.game.GameEvents
import com.luisma.game.ui.views.game.GameViewModel
import com.luisma.game.ui.views.stats.StatsBs
import com.luisma.game.ui.views.stats.StatsViewModel
import com.luisma.game.ui.views.tutorial.TutorialBs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WThemeProvider {
                Surface(
                    modifier = Modifier
                        .background(color = WTheme.colors.background)
                ) {
                    val gameViewModel = hiltViewModel<GameViewModel>()
                    val gameState by gameViewModel.state.collectAsState()
                    // game
                    Game(
                        state = gameState,
                        events = gameViewModel::sendEvent
                    )
                    // stats bs
                    if (gameState.showStats) {
                        val statsViewModel = hiltViewModel<StatsViewModel>()
                        val statsState by statsViewModel.state.collectAsState()
                        StatsBs(
                            onDismiss = {
                                gameViewModel.sendEvent(
                                    GameEvents.HandleStats(showStats = false)
                                )
                            },
                            state = statsState,
                            events = { event -> statsViewModel.sendEvent(event) }
                        )
                    }
                    // tutorial bs
                    if (gameState.showTutorial)
                        TutorialBs(
                            onDismissRequest = {
                                gameViewModel.sendEvent(
                                    GameEvents.HandleTutorial(showTutorial = false)
                                )
                            }
                        )
                }
            }
        }
    }
}

