package com.luisma.game.ui.views.game

sealed class GameEvents {
    object Enter : GameEvents()
    object DeleteChar : GameEvents()
    object FinishHorizontalAnimation : GameEvents()
    data class SendChar(val char: Char) : GameEvents()
    data class OpenStats(val showStats: Boolean) : GameEvents()

    data class FinishAppearAnimation(
        val rowIdx: Int,
        val columnIdx: Int
    ) : GameEvents()

    data class FinishRowAnimation(
        val rowIdx: Int,
    ) : GameEvents()
}