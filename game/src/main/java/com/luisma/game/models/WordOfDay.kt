package com.luisma.game.models

import com.luisma.core.models.WTime

data class WordOfDay(
    val wordId: Int,
    val word: String,
    val wordOfDayAt: WTime,
    val wordOfDayNumber: Int
)