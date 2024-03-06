package com.luisma.core.services.db_services

import com.luisma.core.models.db.UserWordsEntity
import com.luisma.core.models.db.UserWordsPlayingStateContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserWordsSqlService(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dbSqlService: DbSqlService
) {
    suspend fun selectCurrentlyPlaying(): UserWordsEntity? {
        return withContext(dispatcher) {
            try {
                val db = dbSqlService.wordsQueries()
                    .selectCurrentlyPlayingWord().executeAsOne()
                UserWordsEntity.fromDB(db)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun setCurrentlyPlayingWord(
        wordId: Int,
        lastUpdate: String,
        playingStateContract: UserWordsPlayingStateContract
    ): Boolean {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries().setCurrentlyPlayingWord(
                    wordId.toLong(),
                    lastUpdate,
                    playingStateContract.dbValue
                )

                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun unsetCurrentlyPlayingWord(
        wordId: Int
    ): Boolean {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries()
                    .unsetCurrentlyPlayingWord(wordId.toLong())
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun selectWordById(
        wordId: Int
    ): UserWordsEntity? {
        return withContext(dispatcher) {
            try {
                val db = dbSqlService.wordsQueries()
                    .selectUserWordById(wordId.toLong())
                    .executeAsOne()
                UserWordsEntity.fromDB(db)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateWord(
        letters: String,
        lastUpdate: String,
        wordId: Int,
        playingState: UserWordsPlayingStateContract
    ): Boolean {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries().updateWord(
                    letters = letters,
                    last_update = lastUpdate,
                    word_id = wordId.toLong(),
                    playing_state = playingState.dbValue
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun selectPlayedCount(): Int? {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries()
                    .selectGamesPlayedCount()
                    .executeAsOne().toInt()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun selectGamesPlayedCountByPlayingState(
        playingStateContract: UserWordsPlayingStateContract
    ): Int? {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries()
                    .selectGamesPlayedCountByPlayingState(
                        playingStateContract.dbValue
                    )
                    .executeAsOne().toInt()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun selectGamesPlayedByPlayingState(
        limit: Int,
        offset: Int,
        states: List<UserWordsPlayingStateContract>
    ): List<UserWordsEntity> {
        return withContext(dispatcher) {
            try {
                val db = dbSqlService.wordsQueries()
                    .selectGamesPlayedByPlayingState(
                        playing_state = states.map { it.dbValue },
                        limit = limit.toLong(),
                        offset = offset.toLong()
                    ).executeAsList()
                db.map { UserWordsEntity.fromDB(it) }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }


}