package com.luisma.game.models

import javax.annotation.concurrent.Immutable


@Immutable
data class WChar(
    val state: WCharState,
    val char: Char,
    val animationState: WCharAnimationState = WCharAnimationState.Still,
) {
    companion object {
        fun boxEmpty(): WChar {
            return WChar(
                char = Char.MIN_VALUE,
                state = WCharState.Empty
            )
        }

        fun keyboardEmpty(char: Char): WChar {
            return WChar(
                char = char,
                state = WCharState.Empty
            )
        }
    }
}
