package com.luisma.core.models.db
import com.core.db.User_words

data class UserWordsEntity(
    val wordId: Int,
    val letters: String,
    val lastUpdate: String,
    val currentlyPlaying: Boolean,
    val solvedInTime: Boolean,
) {
    companion object {
        fun fromDB(db: User_words): UserWordsEntity {
            return UserWordsEntity(
                wordId = db.word_rowid.toInt(),
                letters = db.letters ?: "",
                lastUpdate = db.last_update ?: "",
                currentlyPlaying = db.currently_playing.toInt() == 1,
                solvedInTime = db.solved_in_time.toInt() == 1
            )
        }
    }
}
