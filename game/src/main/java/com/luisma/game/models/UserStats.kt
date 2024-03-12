package com.luisma.game.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.annotation.concurrent.Immutable

@Immutable
data class UserGamesStats(
    val playedGames: Int,
    val winedPercentage: Int,
    val currentStreak: Int,
    val recordStreak: Int,
    val winDistribution: ImmutableList<UserGameStatsWinDistribution>,
) {
    companion object {
        fun empty(): UserGamesStats {
            return UserGamesStats(
                playedGames = 0,
                winedPercentage = 0,
                currentStreak = 0,
                recordStreak = 0,
                winDistribution = persistentListOf(),
            )
        }
    }
}

@Immutable
data class UserGameStatsWinDistribution(
    val value: Int,
    val percentageWithMaxAsReference: Float
)
