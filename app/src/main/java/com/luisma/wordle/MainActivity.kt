package com.luisma.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.ui.views.game.Game
import com.luisma.game.ui.views.game.GameViewModel
import dagger.hilt.android.AndroidEntryPoint


// Animation
// 1- key entered-> scale box (big and back) [Almost done]
// 2- word no exits -> move left and right the entire row
// 3- word exits -> transform rotation
// 4- word guessed -> after 3, make a wave

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val gameViewModel: GameViewModel by viewModels()
            val state by gameViewModel.state.collectAsState()
            WThemeProvider {
                Game(
                    state = state,
                    events = gameViewModel::sendEvent
                )
            }
        }
    }
}

enum class TriggerState {
    Initial,
    Tap1,
    Tap2
}


@Composable
fun AnimatedRotationBox() {


    var trigger by remember {
        mutableStateOf(TriggerState.Initial)
    }

    WThemeProvider {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = trigger != TriggerState.Initial,
                enter = slideInHorizontally  (
                    animationSpec = tween(durationMillis = 500, delayMillis = 300)
                ) {
                    -1* it
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(Color.Blue)
                        .size(
                            width = 50.dp,
                            height = 50.dp
                        )
                        .padding(
                            end = 20.dp
                        )
                ) {
                    WText(text = "F", color = Color.White, fontSize = 20.sp)
                }
            }

            //trigger
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(50.dp)
                    .offset(
                        x = 0.dp,
                        y = 50.dp
                    )
                    .clickable {
                        trigger =
                            if (trigger == TriggerState.Initial || trigger == TriggerState.Tap2) {
                                TriggerState.Tap1
                            } else {
                                TriggerState.Tap2
                            }
                    }
                    .background(
                        Color.Green
                    ),
            ) {
                WText(text = "Animate")
            }
        }
    }
}
