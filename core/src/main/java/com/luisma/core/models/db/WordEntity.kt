package com.luisma.core.models.db

import com.core.db.Words

data class WordEntity(
    val wordId: Int,
    val word: String,
    val wordOfDay: Boolean,
    val wordOfDayAt: String?,
    val wordOfDayNumber: Int,
) {
    companion object {
        fun fromDB(db: Words): WordEntity {
            return WordEntity(
                wordId = db.word_id.toInt(),
                word = db.word,
                wordOfDay = db.word_of_day.toInt() == 1,
                wordOfDayAt = db.word_of_day_at,
                wordOfDayNumber = db.word_of_day_number?.toInt() ?: 0
            )
        }
    }
}
