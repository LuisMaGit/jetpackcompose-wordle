package com.luisma.game.ui.views.game

sealed class GameViewEvents {
    object Enter : GameViewEvents()
    object DeleteChar : GameViewEvents()
    object FinishHorizontalAnimation : GameViewEvents()
    object GoBack : GameViewEvents()
    object GoToHistoric : GameViewEvents()
    data class SendChar(val char: Char) : GameViewEvents()
    data class HandleStats(val showStats: Boolean) : GameViewEvents()
    data class HandleTutorial(val showTutorial: Boolean) : GameViewEvents()

    data class FinishAppearAnimation(
        val rowIdx: Int,
        val columnIdx: Int
    ) : GameViewEvents()

    data class FinishRowAnimation(
        val rowIdx: Int,
    ) : GameViewEvents()

}