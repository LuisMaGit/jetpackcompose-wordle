package com.luisma.core.models.db

import com.core.db.SelectGamesPlayedByPlayingState

data class JoinWordAndUserWords(
    // word
    val wordId: Int,
    val word: String,
    val wordOfDayAt: String?,
    val wordOfDayNumber: Int,
    // user word
    val letters: String,
    val lastUpdate: String,
    val playingState: UserWordsPlayingStateContract,
) {
    companion object {
        fun fromSelectGamesPlayedByPlayingState(
            db: SelectGamesPlayedByPlayingState
        ): JoinWordAndUserWords {
            return JoinWordAndUserWords(
                wordId = db.word_id?.toInt() ?: 0,
                word = db.word ?: "",
                wordOfDayAt = db.word_of_day_at,
                wordOfDayNumber = db.word_of_day_number?.toInt() ?: 0,
                letters = db.letters ?: "",
                lastUpdate = db.last_update ?: "",
                playingState = UserWordsPlayingStateContract.getContract(
                    dbValue = db.playing_state
                )
            )
        }
    }

}