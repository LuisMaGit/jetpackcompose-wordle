package com.luisma.game.services

import com.luisma.core.models.db.UserStatsEntity
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core.services.db_services.StatsSqlService
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.game.models.LETTERS_WORDS_SEPARATOR
import com.luisma.game.models.UserGameStatsWinDistribution
import com.luisma.game.models.UserGamesStats
import kotlinx.collections.immutable.toImmutableList

class UserStatsService(
    private val userWordsSqlService: UserWordsSqlService,
    private val statsSqlService: StatsSqlService
) {
    private fun getWinedPercentage(
        total: Int,
        target: Int
    ): Int {
        if (total == 0) {
            return 0
        }
        return (target * 100) / total
    }

    fun winDistributionFromStrToInt(values: String): List<Int> {
        val numberListStr = values.split(Regex(LETTERS_WORDS_SEPARATOR.toString()))
        return numberListStr.map {
            try {
                it.toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }
    }

    fun winDistributionFromIntToStr(values: List<Int>): String {
        var output = ""
        values.forEachIndexed { index, value ->
            if (index == values.count() - 1) {
                output += value
            } else {
                output += value.toString() + LETTERS_WORDS_SEPARATOR
            }
        }
        return output
    }

    fun getWinDistributionWithMaxAsReference(
        values: List<Int>
    ): List<UserGameStatsWinDistribution> {
        val max = values.max()
        val output = mutableListOf<UserGameStatsWinDistribution>()
        values.forEach { value ->
            output.add(
                UserGameStatsWinDistribution(
                    value = value,
                    percentageWithMaxAsReference = if (max == 0) 0f else (value.toDouble() / max).toFloat()
                )
            )
        }
        return output
    }

    suspend fun getUserStats(): UserGamesStats {
        val playedGames = userWordsSqlService.selectPlayedCount()
        val solvedInTimeGames = userWordsSqlService.selectGamesPlayedCountByPlayingState(
            UserWordsPlayingStateContract.SOLVED_IN_TIME
        )
        if (playedGames == null || solvedInTimeGames == null) {
            return UserGamesStats.empty()
        }
        val winedPercentage = getWinedPercentage(
            total = playedGames,
            target = solvedInTimeGames
        )

        val statsEntity = statsSqlService.selectUserStats()
        if (statsEntity?.winDistribution == null || statsEntity.winDistribution.isEmpty()) {
            return UserGamesStats.empty()
        }
        val winDistributionInt = winDistributionFromStrToInt(
            values = statsEntity.winDistribution
        )
        val winDistribution = getWinDistributionWithMaxAsReference(
            winDistributionInt
        )
        return UserGamesStats(
            playedGames = playedGames,
            winedPercentage = winedPercentage,
            currentStreak = statsEntity.currentStreak,
            recordStreak = statsEntity.recordStreak,
            winDistribution = winDistribution.toImmutableList(),
        )
    }

    suspend fun setUserStats(
        doneAtTry: Int,
        isAWin: Boolean
    ) {
        val statsEntity = statsSqlService.selectUserStats() ?: return
        // reset current streak if it's a lose
        if (!isAWin) {
            statsSqlService.setUserStats(statsEntity.copy(currentStreak = 0))
            return
        }
        // win
        // add new win values
        val currentStreak = statsEntity.currentStreak + 1
        val recordStreak = if (currentStreak > statsEntity.recordStreak) {
            currentStreak
        } else {
            statsEntity.recordStreak
        }
        val winDistributionInt = winDistributionFromStrToInt(
            statsEntity.winDistribution
        ).toMutableList()
        winDistributionInt[doneAtTry] = winDistributionInt[doneAtTry] + 1
        // put in db
        statsSqlService.setUserStats(
            UserStatsEntity(
                currentStreak = currentStreak,
                recordStreak = recordStreak,
                winDistribution = winDistributionFromIntToStr(winDistributionInt),
                isFirstPlay = statsEntity.isFirstPlay
            )
        )
    }

    suspend fun isFirstPlay(): Boolean {
        return statsSqlService.selectUserStats()?.isFirstPlay == 1
    }

    suspend fun unCheckIsFirstPlay() {
        val statsEntity = statsSqlService.selectUserStats() ?: return
        statsSqlService.setUserStats(statsEntity.copy(isFirstPlay = 0))
    }
}