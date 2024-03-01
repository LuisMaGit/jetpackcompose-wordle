package com.luisma.game

import com.luisma.core.services.NumbService
import com.luisma.core.services.TimeService
import com.luisma.core.services.WordsSqlService
import com.luisma.game.services.GameUtilsService
import com.luisma.game.services.PlayingWordService
import com.luisma.game.services.WordOfDayService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GameModule {

    @Provides
    fun gameUtilService(): GameUtilsService {
        return GameUtilsService()
    }

    @Provides
    fun wordOfDayService(
        timeService: TimeService,
        wordsSqlService: WordsSqlService,
        numbService: NumbService,
    ): WordOfDayService {
        return WordOfDayService(
            timeService = timeService,
            wordsSqlService = wordsSqlService,
            numbService = numbService,
        )
    }

    @Provides
    fun playingWordService(
        timeService: TimeService,
        wordsSqlService: WordsSqlService,
        wordOfDayService: WordOfDayService,
        gameUtilService: GameUtilsService,
    ): PlayingWordService {
        return PlayingWordService(
            timeService = timeService,
            wordsSqlService = wordsSqlService,
            gameUtilService = gameUtilService,
            wordOfDayService = wordOfDayService,
        )
    }
}