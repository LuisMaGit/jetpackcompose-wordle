package com.luisma.core.services

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.core.db.Database
import com.core.db.WordsQueries
import com.luisma.core.models.db.UserWordsEntity
import com.luisma.core.models.db.WordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordsSqlService(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private fun sqlDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = "wordle.db"
        )
    }

    private fun wordsQueries(): WordsQueries {
        return WordsQueries(sqlDriver())
    }

    // user_words
    suspend fun selectCurrentlyPlaying(): UserWordsEntity? {
        return withContext(dispatcher) {
            try {
                val db = wordsQueries().selectCurrentlyPlayingWord().executeAsOne()
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
                wordsQueries().setCurrentlyPlayingWord(
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
                wordsQueries().unsetCurrentlyPlayingWord(
                    wordId.toLong()
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    // words
    suspend fun selectWOD(): WordEntity? {
        return withContext(dispatcher) {
            try {
                val db = wordsQueries()
                    .selectWOD()
                    .executeAsOne()
                WordEntity.fromDB(db)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun selectAvailableWordIDsForNewWOD(): List<Int>? {
        return withContext(dispatcher) {
            try {
                val db = wordsQueries().selectAvailableForNewWOD().executeAsList()
                db.map { it.toInt() }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun unSetWOD(wordId: Int): Boolean {
        return withContext(dispatcher) {
            try {
                wordsQueries().unsetWOD(
                    word_id = wordId.toLong()
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun setWOD(
        time: String,
        wordId: Int,
        wordNumber: Int
    ): Boolean {
        return withContext(dispatcher) {
            try {
                wordsQueries().setWOD(
                    word_of_day_at = time,
                    word_id = wordId.toLong(),
                    word_of_day_number = wordNumber.toLong()
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun selectWordById(wordId: Int): WordEntity? {
        return withContext(dispatcher) {
            try {
                val db = wordsQueries()
                    .selectWordById(wordId.toLong())
                    .executeAsOne()
                WordEntity.fromDB(db)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun searchWord(word: String): List<WordEntity>? {
        return withContext(dispatcher) {
            try {
                val db = wordsQueries().searchWord(word).executeAsList()
                db.map { WordEntity.fromDB(it) }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateWord(
        letters: String,
        lastUpdate: String,
        wordId: Int
    ): Boolean {
        return withContext(dispatcher) {
            try {
                wordsQueries().updateWord(
                    letters = letters,
                    last_update = lastUpdate,
                    word_id = wordId.toLong()
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }


}