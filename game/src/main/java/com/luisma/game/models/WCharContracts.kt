package com.luisma.game.models

enum class WCharAnimationState {
    Still,
    Appear,
    Reveal,
    Success,
}

enum class WCharState {
    NoMatch,
    RightPlace,
    WrongPlace,
    Empty,
    Playing,
}

enum class WCharRowAnimationState {
    Still,
    TriggerHorizontal,
}

val WCHAR_STATE_NOT_RELATED_TO_GUESSED_WORD = setOf(
    WCharState.Empty,
    WCharState.Playing
)