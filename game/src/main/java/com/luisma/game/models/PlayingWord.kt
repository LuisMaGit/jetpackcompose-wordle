package com.luisma.game.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import javax.annotation.concurrent.Immutable

@Immutable
data class PlayingWord(
    val wordId: Int,
    val word: String,
    val wordNumber: Int,
    val lettersRows: LettersRows,
    val lastUpdated: PlayingWordDate,
    val state: PlayingWordGameState,
) {
    companion object {
        fun empty(): PlayingWord {
            return PlayingWord(
                wordId = -1,
                word = "",
                wordNumber = 0,
                lettersRows = persistentMapOf(),
                lastUpdated = PlayingWordDate.noTime(),
                state = PlayingWordGameState.Incomplete,
            )
        }
    }
}

enum class PlayingWordGameState {
    Lose,
    Win,
    Incomplete
}

typealias LettersRows = ImmutableMap<Int, ListCharsWithState>

@Immutable
data class ListCharsWithState(
    val done: Boolean,
    val chars: ImmutableList<WChar>
) {
    companion object {
        fun empty(): ListCharsWithState {
            return ListCharsWithState(
                done = false,
                chars = persistentListOf()
            )
        }
    }
}

