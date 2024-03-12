package com.luisma.core.models.db

import com.core.db.User_words

data class UserWordsEntity(
    val wordId: Int,
    val letters: String,
    val lastUpdate: String,
    val currentlyPlaying: Boolean,
    val playingState: UserWordsPlayingStateContract,
) {
    companion object {
        fun formUserWordsDb(db: User_words): UserWordsEntity {
            return UserWordsEntity(
                wordId = db.word_rowid.toInt(),
                letters = db.letters ?: "",
                lastUpdate = db.last_update ?: "",
                currentlyPlaying = db.currently_playing.toInt() == 1,
                playingState = UserWordsPlayingStateContract.getContract(
                    dbValue = db.playing_state
                )
            )
        }
    }
}

enum class UserWordsPlayingStateContract(
    val dbValue: Long
) {
    SOLVED_IN_TIME(dbValue = 1),
    SOLVED_NOT_IN_TIME(dbValue = 2),
    PLAYING(dbValue = 3),
    LOSE(dbValue = 4);

    companion object {
        fun getContract(dbValue: Long): UserWordsPlayingStateContract {
            return UserWordsPlayingStateContract.values().first { it.dbValue == dbValue }
        }
    }

}
