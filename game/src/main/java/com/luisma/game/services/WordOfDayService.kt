package com.luisma.game.services

import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core.models.db.WordEntity
import com.luisma.core.services.NumbService
import com.luisma.core.services.TimeService
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.core.services.db_services.WordsSqlService
import com.luisma.game.models.WordOfDay

class WordOfDayService(
    private val timeService: TimeService,
    private val wordsSqlService: WordsSqlService,
    private val userStatsService: UserStatsService,
    private val userWordsSqlService: UserWordsSqlService
) {

    private fun mapWODEntityToWOD(entity: WordEntity?): WordOfDay? {
        if (entity == null) {
            return null
        }
        val time = timeService.fromStrToWTime(
            value = entity.wordOfDayAt ?: ""
        ) ?: return null
        return WordOfDay(
            wordId = entity.wordId,
            word = entity.word,
            wordOfDayAt = time,
            wordOfDayNumber = entity.wordOfDayNumber
        )
    }

    private suspend fun deprecateCurrentAndSetNewWOD(
        oldWODId: Int,
        oldWODNumber: Int
    ): WordOfDay? {
        if (!wordsSqlService.unSetWOD(oldWODId)) {
            return null
        }
        return setAndGetWOD(
            wordOfDayNumber = oldWODNumber + 1
        )
    }

    private suspend fun setAndGetWOD(
        wordOfDayNumber: Int
    ): WordOfDay? {
        val id = wordsSqlService.selectAvailableWordIDsForNewWOD() ?:         return null
        // set new WOD
        val now = timeService.getWTimeNow()
        val nowStr = timeService.fromWTimeToStr(now)
        wordsSqlService.setWOD(
            time = nowStr,
            wordId = id,
            wordNumber = wordOfDayNumber
        )
        // get it
        val wodEntity = wordsSqlService.selectWOD()
        return mapWODEntityToWOD(wodEntity)
    }

    suspend fun getWOD(): WordOfDay? {
        // no WOD -> set first WOD
        val entityWOD = wordsSqlService.selectWOD() ?: return setAndGetWOD(wordOfDayNumber = 1)
        val wod = mapWODEntityToWOD(entityWOD) ?: return null
        // WOD still valid -> get WOD
        if (timeService.itHasNotBeen24HoursSince(wod.wordOfDayAt)) {
            return wod
        }

        // update user stats if the word was not solved
        val playingState = userWordsSqlService.selectWordById(wordId = wod.wordId)?.playingState
        if (playingState != null &&
            playingState != UserWordsPlayingStateContract.SOLVED_IN_TIME
        ) {
            userStatsService.setUserStats(doneAtTry = 0, isAWin = false)
        }
        // WOD deprecated -> remove current and set WOD
        return deprecateCurrentAndSetNewWOD(
            oldWODId = wod.wordId,
            oldWODNumber = wod.wordOfDayNumber
        )
    }
}


