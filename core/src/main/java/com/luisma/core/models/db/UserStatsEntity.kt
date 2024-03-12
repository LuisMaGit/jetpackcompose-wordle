package com.luisma.core.models.db

import com.core.db.User_stats

data class UserStatsEntity(
    val currentStreak: Int,
    val recordStreak: Int,
    val winDistribution: String,
    val isFirstPlay: Int,
) {
    companion object {
        fun fromDB(db: User_stats): UserStatsEntity {
            return UserStatsEntity(
                currentStreak = db.current_streak?.toInt() ?: 0,
                recordStreak = db.record_streak?.toInt() ?: 0,
                winDistribution = db.win_distribution ?: "",
                isFirstPlay = db.is_first_play?.toInt() ?: 0,
            )
        }
    }
}