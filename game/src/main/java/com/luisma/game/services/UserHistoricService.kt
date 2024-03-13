package com.luisma.game.services

import com.luisma.core.models.WTime
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core.services.PaginationService
import com.luisma.core.services.TimeService
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.core.services.db_services.WordsSqlService
import com.luisma.game.models.GameDate
import com.luisma.game.models.MAX_NUMBER_OF_TRIES_TO_GUESS
import com.luisma.game.models.UserHistoricFilterCount
import com.luisma.game.models.UserHistoricWord
import com.luisma.game.models.UserHistoricWordsPage
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class UserHistoricService(
    private val userWordsSqlService: UserWordsSqlService,
    private val paginationService: PaginationService,
    private val gameUtilsService: GameUtilsService,
    private val wordSqlService: WordsSqlService,
    private val timeService: TimeService
) {

    private fun mapEntityToUserHistoricWord(
        letters: String,
        lastUpdate: String,
        toGuessWord: String,
        wordId: Int,
        playingState: UserWordsPlayingStateContract,
        wordOfDayId: Int,
    ): UserHistoricWord {

        // date
        val wTime = timeService.fromStrToWTime(lastUpdate) ?: WTime.noTime()
        val date = GameDate.fromWTime(wTime)

        // try number
        val letterList = gameUtilsService.splitWordsWithSeparators(
            wordsWithSeparators = letters
        )
        val tryNumber = letterList.count()

        // lastChars
        val lastChars = if (letterList.isEmpty() || letterList.first().isEmpty()) {
            gameUtilsService.getFullRowEmpty()
        } else {
            gameUtilsService.resolveLettersState(
                wordsWithSeparators = letterList.last(),
                toGuessWord = toGuessWord,
                fillEmptyRows = false
            ).values.single()
        }


        return UserHistoricWord(
            lastChars = lastChars,
            state = playingState,
            date = date,
            tryNumber = tryNumber,
            maxTries = MAX_NUMBER_OF_TRIES_TO_GUESS,
            wordId = wordId,
            isWOD = wordId == wordOfDayId
        )
    }

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
            solvedNotOnTime = values[UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME.ordinal],
            playing = values[UserWordsPlayingStateContract.PLAYING.ordinal],
            lost = values[UserWordsPlayingStateContract.LOSE.ordinal],
        )
    }

    suspend fun getHistoricWords(
        page: Int,
        filter: List<UserWordsPlayingStateContract>
    ): UserHistoricWordsPage {
        val paginationSql = paginationService.toSql(page)
        val entityWords = userWordsSqlService.selectGamesPlayedByPlayingState(
            states = filter,
            offset = paginationSql.offset,
            limit = paginationSql.limit,
        )

        if (entityWords.isEmpty())
            return UserHistoricWordsPage.empty()

        val wod = wordSqlService.selectWOD()
        return UserHistoricWordsPage(
            isLastPage = paginationService.isLastPage(entityWords.count()),
            data = entityWords.map { entity ->
                mapEntityToUserHistoricWord(
                    letters = entity.letters,
                    toGuessWord = entity.word,
                    playingState = entity.playingState,
                    lastUpdate = entity.lastUpdate,
                    wordId = entity.wordId,
                    wordOfDayId = wod?.wordId ?: 0
                )
            }.toImmutableList()
        )
    }

}