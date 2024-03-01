package com.luisma.game.services

import com.luisma.core.models.WDuration
import com.luisma.core.models.WTime
import com.luisma.core.models.db.UserWordsEntity
import com.luisma.core.services.TimeService
import com.luisma.core.services.WordsSqlService
import com.luisma.game.models.PlayingWord
import com.luisma.game.models.PlayingWordDate
import com.luisma.game.models.WordOfDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayingWordService(
    private val timeService: TimeService,
    private val wordsSqlService: WordsSqlService,
    private val wordOfDayService: WordOfDayService,
    private val gameUtilService: GameUtilsService
) {

    private suspend fun mapCurrentlyPlayingEntityToPlayingWord(
        entity: UserWordsEntity?
    ): PlayingWord? {
        if (entity == null) {
            return null
        }

        val fullWordEntity = wordsSqlService.selectWordById(
            entity.wordId
        ) ?: return null

        val lettersRow = gameUtilService.resolveLettersState(
            entity.letters,
            fullWordEntity.word
        )
        val lastUpdate = timeService.fromStrToWTime(
            entity.lastUpdate
        ) ?: timeService.getWTimeNow()

        val state = gameUtilService.getPlayingWordGameState(
            lettersRow = lettersRow
        )

        val word = fullWordEntity.word

        return PlayingWord(
            wordId = fullWordEntity.wordId,
            word = word,
            wordNumber = fullWordEntity.wordOfDayNumber,
            lettersRows = lettersRow,
            lastUpdated = PlayingWordDate.fromWTime(lastUpdate),
            state = state,
        )
    }

    private suspend fun saveWODasCurrentlyPlayingAndGet(
        wordOfDay: WordOfDay
    ): PlayingWord? {
        val responseSetCurrentlyPlaying = wordsSqlService.setCurrentlyPlayingWord(
            wordId = wordOfDay.wordId,
            lastUpdate = timeService.fromWTimeToStr(timeService.getWTimeNow())
        )
        if (!responseSetCurrentlyPlaying) {
            return null
        }
        return mapCurrentlyPlayingEntityToPlayingWord(
            wordsSqlService.selectCurrentlyPlaying()
        )
    }

    /**
     * returns the currently playing word of the user
     * if the word not exits gets the WOD and it's saved as currently playing
     * if exits and it is the WOD then is returned
     * if exits and it is not the WOD is updated the currently playing word with the WOD
     */
    suspend fun getCurrentlyPlaying(): PlayingWord? {
        val entityCurrentlyPlaying = wordsSqlService.selectCurrentlyPlaying()

        // no playing word in db
        if (entityCurrentlyPlaying == null) {
            // get wod
            val wordOfDay = wordOfDayService.getWOD() ?: return null
            // save in historic as current
            return saveWODasCurrentlyPlayingAndGet(wordOfDay)
        }

        // playing word in db and it IS WOD -> keep playing
        val wordOfDay = wordOfDayService.getWOD() ?: return null
        if (entityCurrentlyPlaying.wordId == wordOfDay.wordId) {
            return mapCurrentlyPlayingEntityToPlayingWord(
                entityCurrentlyPlaying
            )
        }

        // playing word in db and it is NOT WOD -> update currently playing
        val responseRemoveCurrentlyPlaying = wordsSqlService.unsetCurrentlyPlayingWord(
            entityCurrentlyPlaying.wordId
        )
        if (!responseRemoveCurrentlyPlaying) return null
        // save in historic as current
        return saveWODasCurrentlyPlayingAndGet(wordOfDay)
    }

    /**
     * check if the [charsGuessed] is an accepted word (if exists in the db)
     */
    suspend fun resolveGuessedWord(
        guessedWord: String
    ): Boolean {
        val responseSql = wordsSqlService.searchWord(guessedWord)
        return !responseSql.isNullOrEmpty()
    }

    /**
     * returns a flow type [WDuration], with a decreasing
     * 1 sec timer until the next WOD
     */
    suspend fun countdownNextWOD(wordId: Int): Flow<WDuration> {
        val wod = wordsSqlService.selectWordById(wordId)
        val lastUpdate = timeService.fromStrToWTime(
            wod?.wordOfDayAt ?: ""
        ) ?: WTime.noTime()

        val twenty4HoursAfterWOD = timeService.timePlus24Hours(
            lastUpdate
        )
        var duration = timeService.durationFromNowUntil(
            twenty4HoursAfterWOD
        )
        return flow {
            while (true) {
                duration = timeService.durationMinus1Sec(duration)
                emit(duration)
                delay(1000)
            }
        }
    }

    /**
     * update the currently playing word in the db
     */
    suspend fun updateCurrentlyPlayingWord(
        wordsWithSeparators: String,
        wordId: Int
    ): Boolean {
        val lastUpdate = timeService.fromWTimeToStr(
            timeService.getWTimeNow()
        )
        return wordsSqlService.updateWord(
            letters = wordsWithSeparators,
            wordId = wordId,
            lastUpdate = lastUpdate
        )
    }

}