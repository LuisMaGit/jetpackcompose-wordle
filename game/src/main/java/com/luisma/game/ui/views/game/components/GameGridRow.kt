package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.luisma.core_ui.components.CharBox
import com.luisma.core_ui.components.CharBoxAnimationType
import com.luisma.core_ui.components.CharBoxDimensions
import com.luisma.core_ui.components.CharBoxType
import com.luisma.core_ui.theme.WSpacing
import com.luisma.game.models.WChar
import com.luisma.game.models.WCharAnimationState
import com.luisma.game.models.WCharState
import kotlinx.collections.immutable.ImmutableList

internal object GameGridRowConstants {
    val spacing = WSpacing.k5
}

@Composable
internal fun GameGridRow(
    modifier: Modifier = Modifier,
    chars: ImmutableList<WChar>,
    boxDimension: CharBoxDimensions,
    verticalOffset: Dp = 0.dp,
    rowIdx: Int = 0,
    onDismissScaleAnimation: ((rowIdx: Int, charIdx: Int) -> Unit)? = null,
    onDismissRowAnimation: ((rowIdx: Int) -> Unit)? = null,
) {

    fun charStateMapper(charState: WCharState): CharBoxType =
        when (charState) {
            WCharState.Empty -> CharBoxType.Empty
            WCharState.RightPlace -> CharBoxType.CharOk
            WCharState.WrongPlace -> CharBoxType.CharMisplaced
            WCharState.NoMatch -> CharBoxType.CharMissing
            WCharState.Playing -> CharBoxType.Char
        }

    fun charAnimationMapper(animation: WCharAnimationState): CharBoxAnimationType {
        return when (animation) {
            WCharAnimationState.Still -> CharBoxAnimationType.NoAnimation
            WCharAnimationState.Appear -> CharBoxAnimationType.Scale
            WCharAnimationState.Reveal -> CharBoxAnimationType.Rotate
            WCharAnimationState.Success -> CharBoxAnimationType.Jump
        }
    }

    fun xOffset(columnIdx: Int): Dp {
        if (columnIdx == 0) {
            return 0.dp
        }

        return (boxDimension.size + GameGridRowConstants.spacing) * columnIdx
    }


    chars.forEachIndexed { columnIdx, wChar ->
        CharBox(
            modifier = modifier
                .offset(
                    x = xOffset(columnIdx),
                    y = verticalOffset
                ),
            charAnimation = charAnimationMapper(wChar.animationState),
            charState = charStateMapper(wChar.state),
            char = wChar.char,
            dimensions = boxDimension,
            charIdx = columnIdx,
            lastCharIdx = chars.size - 1,
            rowIdx = rowIdx,
            onDismissScaleAnimation = onDismissScaleAnimation,
            onDismissRowAnimation = onDismissRowAnimation,
        )
    }
}