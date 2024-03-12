package com.luisma.core.services.db_services

import com.luisma.core.models.db.WordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordsSqlService(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dbSqlService: DbSqlService
) {

    suspend fun selectWOD(): WordEntity? {
        return withContext(dispatcher) {
            try {
                val db = dbSqlService.wordsQueries()
                    .selectWOD()
                    .executeAsOne()
                WordEntity.fromDB(db)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun selectAvailableWordIDsForNewWOD(): Int? {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries()
                    .selectAvailableForNewWOD().executeAsOne().toInt()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun unSetWOD(wordId: Int): Boolean {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries()
                    .unsetWOD(word_id = wordId.toLong())
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
                dbSqlService.wordsQueries().setWOD(
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
                val db = dbSqlService.wordsQueries()
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
                val db = dbSqlService.wordsQueries()
                    .searchWord(word).executeAsList()
                db.map { WordEntity.fromDB(it) }
            } catch (e: Exception) {
                null
            }
        }
    }

}