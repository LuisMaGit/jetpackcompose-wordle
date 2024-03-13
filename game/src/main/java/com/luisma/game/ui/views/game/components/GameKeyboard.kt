package com.luisma.game.ui.views.game.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.luisma.core_ui.components.CharKey
import com.luisma.core_ui.components.CharKeyDimensions
import com.luisma.core_ui.components.CharKeySubmit
import com.luisma.core_ui.components.CharKeySubmitDimensions
import com.luisma.core_ui.components.CharKeySubmitType
import com.luisma.core_ui.components.CharKeyType
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.game.models.KeyboardState
import com.luisma.game.models.WCharState
import com.luisma.game.models.WKeyboard
import com.luisma.game.models.WKeyboardKeyState

@Composable
internal fun GameKeyboard(
    modifier: Modifier = Modifier,
    keyDimensions: CharKeyDimensions,
    keySubmitDimensions: CharKeySubmitDimensions,
    keyboard: WKeyboard,
    onTapEnter: () -> Unit,
    onTapDelete: () -> Unit,
    onTapChar: (char: Char) -> Unit,
    enabledKeyState: WKeyboardKeyState,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KeysRows(
            keys = keyboard.row3,
            keyDimensions = keyDimensions,
            keySubmitDimensions = keySubmitDimensions,
            onTapDelete = onTapDelete,
            onTapEnter = onTapEnter,
            enabledKeyState = enabledKeyState,
            onTapChar = onTapChar,
        )
        KeysRows(
            keys = keyboard.row2,
            keyDimensions = keyDimensions,
            keySubmitDimensions = keySubmitDimensions,
            onTapDelete = onTapDelete,
            onTapEnter = onTapEnter,
            enabledKeyState = enabledKeyState,
            onTapChar = onTapChar,
        )
        KeysRows(
            keys = keyboard.row1,
            keyDimensions = keyDimensions,
            keySubmitDimensions = keySubmitDimensions,
            withEnterAndDelete = true,
            onTapDelete = onTapDelete,
            onTapEnter = onTapEnter,
            enabledKeyState = enabledKeyState,
            onTapChar = onTapChar,
        )
    }
}

@Composable
private fun KeysRows(
    modifier: Modifier = Modifier,
    keys: KeyboardState,
    withEnterAndDelete: Boolean = false,
    keyDimensions: CharKeyDimensions,
    keySubmitDimensions: CharKeySubmitDimensions,
    onTapEnter: () -> Unit,
    onTapDelete: () -> Unit,
    onTapChar: (char: Char) -> Unit,
    enabledKeyState: WKeyboardKeyState,
) {

    fun charKeyTypeMapper(state: WCharState): CharKeyType {
        return when (state) {
            WCharState.WrongPlace -> CharKeyType.CharMisplaced
            WCharState.RightPlace -> CharKeyType.CharOk
            WCharState.NoMatch -> CharKeyType.CharMissing
            else -> CharKeyType.Empty
        }
    }

    Row(
        modifier = modifier
    ) {
        if (withEnterAndDelete) {
            CharKeySubmit(
                modifier = Modifier.padding(
                    end = WSpacing.k5,
                    bottom = WSpacing.k5
                ),
                dimension = keySubmitDimensions,
                type = CharKeySubmitType.Enter,
                onTap = onTapEnter,
                enabled = enabledKeyState.enabledEnter
            )
        }
        keys.forEach { wChar ->
            CharKey(
                modifier = Modifier.padding(
                    end = WSpacing.k5,
                    bottom = WSpacing.k5
                ),
                dimensions = keyDimensions,
                charKeyType = charKeyTypeMapper(wChar.state),
                char = wChar.char,
                enabled = enabledKeyState.enableAdd,
                onTapChar = onTapChar,
            )
        }
        if (withEnterAndDelete) {
            CharKeySubmit(
                modifier = Modifier.padding(
                    end = WSpacing.k5,
                    bottom = WSpacing.k5
                ),
                dimension = keySubmitDimensions,
                type = CharKeySubmitType.Delete,
                onTap = onTapDelete,
                enabled = enabledKeyState.enabledDelete
            )
        }
    }
}


@Preview
@Composable
private fun GameKeyboardPreview() {
    WThemeProvider {
        GameKeyboard(
            keyboard = keyboardMock,
            keyDimensions = CharKeyDimensions.small(),
            keySubmitDimensions = CharKeySubmitDimensions.small(),
            enabledKeyState = WKeyboardKeyState.allDisabled(),
            onTapEnter = {},
            onTapDelete = {},
            onTapChar = {},
        )
    }
}