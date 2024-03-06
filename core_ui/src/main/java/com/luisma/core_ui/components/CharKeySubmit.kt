package com.luisma.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.R
import com.luisma.core_ui.helpers.ScreenSizeBreakPoints
import com.luisma.core_ui.helpers.fontSizeNonScaledSp
import com.luisma.core_ui.theme.WBorderRadiusContract
import com.luisma.core_ui.theme.WIconSizeContract
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider

enum class CharKeySubmitType {
    Enter,
    Delete,
}

@Stable
data class CharKeySubmitDimensions(
    val width: Dp,
    val height: Dp,
    val fontSize: Int,
) {
    companion object {
        fun small(): CharKeySubmitDimensions {
            return CharKeySubmitDimensions(
                width = 48.dp,
                height = 58.dp,
                fontSize = 12
            )
        }

        private fun big(): CharKeySubmitDimensions {
            return CharKeySubmitDimensions(
                width = 90.dp,
                height = 58.dp,
                fontSize = 16
            )
        }

        fun keySubmitDimensions(
            screenSizeBreakPoints: ScreenSizeBreakPoints
        ): CharKeySubmitDimensions {
            return when (screenSizeBreakPoints) {
                ScreenSizeBreakPoints.Mobile -> small()
                ScreenSizeBreakPoints.Tablet -> big()
            }
        }
    }
}

@Composable
fun CharKeySubmit(
    modifier: Modifier = Modifier,
    dimension: CharKeySubmitDimensions = CharKeySubmitDimensions.small(),
    type: CharKeySubmitType,
    onTap: () -> Unit,
    enabled: Boolean,
) {

    Box(
        modifier = modifier
            .size(
                width = dimension.width,
                height = dimension.height
            )
            .clip(
                shape = RoundedCornerShape(WBorderRadiusContract.k6)
            )
            .background(
                color = WTheme.colors.keyDefault,
            )
            .clickable(enabled = enabled) { onTap() },
        contentAlignment = Alignment.Center
    ) {
        when (type) {
            CharKeySubmitType.Delete -> SvgImage(
                modifier = Modifier
                    .size(
                        WIconSizeContract.k24
                    ),
                id = R.drawable.ic_backsapce,
                tint = WTheme.colors.inverseBackground
            )

            CharKeySubmitType.Enter -> WText(
                text = stringResource(id = R.string.game_enter_key),
                wTextType = WTextType.T2,
                fontSize = dimension.fontSize.fontSizeNonScaledSp
            )
        }
    }
}

@Preview
@Composable
private fun CharKeySubmitPreview() {
    WThemeProvider {
        Column {
            CharKeySubmit(
                type = CharKeySubmitType.Enter,
                enabled = false,
                onTap = {}
            )
            CharKeySubmit(
                type = CharKeySubmitType.Delete,
                enabled = false,
                onTap = {}
            )
        }
    }
}

@Preview
@Composable
private fun CharKeySubmitDarkPreview() {
    WThemeProvider(
        darkTheme = true
    ) {
        Column {
            CharKeySubmit(
                type = CharKeySubmitType.Enter,
                enabled = false,
                onTap = {}
            )
            CharKeySubmit(
                type = CharKeySubmitType.Delete,
                enabled = false,
                onTap = {}
            )
        }
    }
}