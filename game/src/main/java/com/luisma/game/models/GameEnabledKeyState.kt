package com.luisma.game.models

import javax.annotation.concurrent.Immutable

@Immutable
data class GameEnabledKeyState(
    val enabledDelete: Boolean,
    val enabledEnter: Boolean,
    val enableAdd: Boolean,
) {
    companion object {
        fun allDisabled(): GameEnabledKeyState {
            return GameEnabledKeyState(
                enabledDelete = false,
                enableAdd = false,
                enabledEnter = false,
            )
        }
    }
}