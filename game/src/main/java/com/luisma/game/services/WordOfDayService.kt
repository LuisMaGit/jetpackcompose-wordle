package com.luisma.game.services

import com.luisma.core.models.db.WordEntity
import com.luisma.core.services.NumbService
import com.luisma.core.services.TimeService
import com.luisma.core.services.WordsSqlService
import com.luisma.game.models.WordOfDay

class WordOfDayService(
    private val timeService: TimeService,
    private val wordsSqlService: WordsSqlService,
    private val numbService: NumbService
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

    private suspend fun getNewWODIdentifier(): Int? {
        // if no exits returns null
        val availableIdsForNewWOD = wordsSqlService.selectAvailableWordIDsForNewWOD() ?: return null
        // check when it is only one
        val amount = availableIdsForNewWOD.count() - 1
        if (amount == 0) {
            return availableIdsForNewWOD[0]
        }
        // most of the time behavior
        val idxNewWOD = numbService.nextPositiveIntUnit(amount)
        return availableIdsForNewWOD[idxNewWOD]
    }

    private suspend fun setAndGetWOD(
        wordOfDayNumber: Int
    ): WordOfDay? {
        // no more available identifiers
        val id = getNewWODIdentifier() ?: return null
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
        // WOD deprecated -> remove current and set WOD
        return deprecateCurrentAndSetNewWOD(
            oldWODId = wod.wordId,
            oldWODNumber = wod.wordOfDayNumber
        )
    }
}


