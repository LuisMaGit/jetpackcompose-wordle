package com.luisma.core.models.db

import com.core.db.User_stats

data class UserStatsEntity(
    val currentStreak: Int,
    val recordStreak: Int,
    val winDistribution: String,
) {
    companion object {
        fun fromDB(db: User_stats): UserStatsEntity {
            return UserStatsEntity(
                currentStreak = db.current_streak?.toInt() ?: 0,
                recordStreak = db.record_streak?.toInt() ?: 0,
                winDistribution = db.win_distribution ?: ""
            )
        }
    }
}