package com.luisma.game.services

import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.game.models.UserHistoricFilterCount
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class UserHistoricService(
    private val userWordsSqlService: UserWordsSqlService
) {

    suspend fun getFilterCount(): UserHistoricFilterCount {
        val deferred = mutableListOf<Deferred<Int>>()
        val values = coroutineScope {
            for (state in UserWordsPlayingStateContract.values()) {
                deferred.add(
                    async {
                        userWordsSqlService.selectGamesPlayedCountByPlayingState(state) ?: 0
                    }
                )
            }
            deferred.awaitAll()
        }

        return UserHistoricFilterCount(
            solvedOnTime = values[UserWordsPlayingStateContract.SOLVED_IN_TIME.ordinal],
            solvedNotOnTime =values[UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME.ordinal],
            playing = values[UserWordsPlayingStateContract.PLAYING.ordinal],
            lost = values[UserWordsPlayingStateContract.LOSE.ordinal],
        )
    }

}