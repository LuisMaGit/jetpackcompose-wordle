package com.luisma.game.models

import javax.annotation.concurrent.Immutable

@Immutable
data class WKeyboardKeyState(
    val enabledDelete: Boolean,
    val enabledEnter: Boolean,
    val enableAdd: Boolean,
) {
    companion object {
        fun allDisabled(): WKeyboardKeyState {
            return WKeyboardKeyState(
                enabledDelete = false,
                enableAdd = false,
                enabledEnter = false,
            )
        }
    }
}