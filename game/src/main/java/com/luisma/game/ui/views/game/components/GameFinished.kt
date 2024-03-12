package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.luisma.core_ui.R
import com.luisma.core_ui.components.WText
import com.luisma.game.ui.views.game.GameViewEvents
import com.luisma.game.ui.views.game.GameViewState


@Composable
internal fun GameFinished(
    state: GameViewState,
    events: (event: GameViewEvents) -> Unit,
) {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameAppbar(
            onTapStats = { events(GameViewEvents.HandleStats(showStats = true)) },
            onTapTutorial = { events(GameViewEvents.HandleTutorial(showTutorial = true)) },
            onTapHistory = { events(GameViewEvents.GoToHistoric) },
            onTapBack = { events(GameViewEvents.GoBack) },
            viewType = state.gameType
        )
        Spacer(modifier = Modifier.weight(1f))
        WText(stringResource(id = R.string.game_finish))
        Spacer(modifier = Modifier.weight(1f))
    }
}