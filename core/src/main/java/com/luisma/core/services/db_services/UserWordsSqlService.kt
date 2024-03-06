package com.luisma.core.services.db_services

import com.luisma.core.models.db.UserWordsEntity
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
        lastUpdate: String
    ): Boolean {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries()
                    .setCurrentlyPlayingWord(
                        wordId.toLong(),
                        lastUpdate
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
        solvedInTime: Boolean
    ): Boolean {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries().updateWord(
                    letters = letters,
                    last_update = lastUpdate,
                    word_id = wordId.toLong(),
                    solved_in_time = if (solvedInTime) 1 else 0
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

    suspend fun selectSolvedPlayedCount(): Int? {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries()
                    .selectSolvedGamesPlayedCount()
                    .executeAsOne().toInt()
            } catch (e: Exception) {
                null
            }
        }
    }
}