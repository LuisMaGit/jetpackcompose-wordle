package com.luisma.game.ui.utils

import com.luisma.core_ui.components.CharBoxType
import com.luisma.game.models.WCharState

fun uiCharStateMapper(charState: WCharState): CharBoxType =
    when (charState) {
        WCharState.Empty -> CharBoxType.Empty
        WCharState.RightPlace -> CharBoxType.CharOk
        WCharState.WrongPlace -> CharBoxType.CharMisplaced
        WCharState.NoMatch -> CharBoxType.CharMissing
        WCharState.Playing -> CharBoxType.Char
    }