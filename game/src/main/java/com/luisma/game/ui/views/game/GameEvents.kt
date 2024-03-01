package com.luisma.game.ui.views.game

sealed class GameEvents {
    data class SendChar(val char: Char) : GameEvents()
    object Enter : GameEvents()
    object DeleteChar : GameEvents()
}