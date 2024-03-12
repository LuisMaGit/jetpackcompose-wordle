package com.luisma.game.ui.views.tutorial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.R
import com.luisma.core_ui.components.CharBoxDimensions
import com.luisma.core_ui.components.WBottomSheet
import com.luisma.core_ui.components.WText
import com.luisma.core_ui.theme.WFontSize
import com.luisma.core_ui.theme.WScreenFractions
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.models.WChar
import com.luisma.game.models.WCharState
import com.luisma.game.ui.views.game.components.GameGridRow
import com.luisma.game.ui.views.tutorial.components.TutorialBulletPointText
import com.luisma.game.ui.views.tutorial.components.TutorialHighlightCharText
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TutorialView() {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(WScreenFractions.k90)
                .align(Alignment.TopCenter)
                .verticalScroll(scrollState)
        ) {
            // title
            WText(
                text = stringResource(id = R.string.tutorial_title),
                fontSize = WFontSize.k32
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k5))
            // subtitle
            WText(
                text = stringResource(id = R.string.tutorial_subtitle),
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))
            // bullet points
            TutorialBulletPointText(
                text = stringResource(id = R.string.tutorial_bullet_point_1)
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k5))
            TutorialBulletPointText(
                text = stringResource(id = R.string.tutorial_bullet_point_2)
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k10))
            WText(
                text = stringResource(id = R.string.tutorial_example_title),
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))
            // example 1
            Box {
                GameGridRow(
                    chars = persistentListOf(
                        WChar(state = WCharState.RightPlace, char = 'A'),
                        WChar(state = WCharState.Playing, char = 'C'),
                        WChar(state = WCharState.Playing, char = 'T'),
                        WChar(state = WCharState.Playing, char = 'O'),
                        WChar(state = WCharState.Playing, char = 'R'),
                    ),
                    boxDimension = CharBoxDimensions.small()
                )
            }
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k10))
            TutorialHighlightCharText(
                char = "A",
                text = stringResource(id = R.string.tutorial_example_1)
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))
            // example 2
            Box {
                GameGridRow(
                    chars = persistentListOf(
                        WChar(state = WCharState.Playing, char = 'D'),
                        WChar(state = WCharState.WrongPlace, char = 'E'),
                        WChar(state = WCharState.Playing, char = 'N'),
                        WChar(state = WCharState.Playing, char = 'S'),
                        WChar(state = WCharState.Playing, char = 'O'),
                    ),
                    boxDimension = CharBoxDimensions.small()
                )
            }
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k10))
            TutorialHighlightCharText(
                char = "E",
                text = stringResource(id = R.string.tutorial_example_2)
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k18))
            // example 2
            Box {
                GameGridRow(
                    chars = persistentListOf(
                        WChar(state = WCharState.Playing, char = 'F'),
                        WChar(state = WCharState.Playing, char = 'E'),
                        WChar(state = WCharState.Playing, char = 'R'),
                        WChar(state = WCharState.NoMatch, char = 'I'),
                        WChar(state = WCharState.Playing, char = 'A'),
                    ),
                    boxDimension = CharBoxDimensions.small()
                )
            }
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k10))
            TutorialHighlightCharText(
                char = "I",
                text = stringResource(id = R.string.tutorial_example_3)
            )
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k40))
            // footer
            WText(
                text = stringResource(id = R.string.tutorial_new_word),
            )
        }
    }
}

@Composable
fun TutorialViewBs(
    onDismissRequest: () -> Unit
) {
    WBottomSheet(
        show = true,
        onDismissRequest = onDismissRequest
    ) {
        TutorialView()
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun TutorialViewPrev() {
    WThemeProvider(
        darkTheme = true
    ) {
        Surface(
            color =  WTheme.colors.background
        ) {
            TutorialView()
        }
    }
}