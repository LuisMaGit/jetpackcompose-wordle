package com.luisma.game.models

import javax.annotation.concurrent.Immutable

@Immutable
data class GameCursorPosition(
    val row: Int,
    val column: Int
) {
    companion object {
        fun initial(): GameCursorPosition {
            return GameCursorPosition(
                row = 0,
                column = 0
            )
        }
    }
}
