package com.luisma.game.models

import com.luisma.core.models.db.UserWordsPlayingStateContract
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.annotation.concurrent.Immutable

@Immutable
data class UserHistoricWordsPage(
    val data: ImmutableList<UserHistoricWord>,
    val isLastPage: Boolean,
) {
    companion object {
        fun empty(): UserHistoricWordsPage {
            return UserHistoricWordsPage(
                data = persistentListOf(),
                isLastPage = true
            )
        }
    }
}

@Immutable
data class UserHistoricWord(
    val lastChars: ListCharsWithState,
    val state: UserWordsPlayingStateContract,
    val date: GameDate,
    val tryNumber: Int,
    val maxTries: Int,
    val wordId: Int,
    val isWOD: Boolean
)