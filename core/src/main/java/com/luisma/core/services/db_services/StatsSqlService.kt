package com.luisma.core.services.db_services

import com.luisma.core.models.db.UserStatsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatsSqlService(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dbSqlService: DbSqlService
) {

    suspend fun selectUserStats(): UserStatsEntity? {
        return withContext(dispatcher) {
            try {
                val db = dbSqlService.wordsQueries().selectUserStats().executeAsOne()
                UserStatsEntity.fromDB(db)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun setUserStats(
        stats: UserStatsEntity
    ): Boolean {
        return withContext(dispatcher) {
            try {
                dbSqlService.wordsQueries().setUserStats(
                    current_streak = stats.currentStreak.toLong(),
                    record_streak = stats.recordStreak.toLong(),
                    win_distribution = stats.winDistribution
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}