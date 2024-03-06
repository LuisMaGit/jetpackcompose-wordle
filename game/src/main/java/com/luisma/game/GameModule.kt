package com.luisma.game

import com.luisma.core.services.NumbService
import com.luisma.core.services.TimeService
import com.luisma.core.services.db_services.StatsSqlService
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.core.services.db_services.WordsSqlService
import com.luisma.game.services.GameUtilsService
import com.luisma.game.services.PlayingWordService
import com.luisma.game.services.UserStatsService
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
        userStatsService: UserStatsService,
        userWordsSqlService: UserWordsSqlService
    ): WordOfDayService {
        return WordOfDayService(
            timeService = timeService,
            wordsSqlService = wordsSqlService,
            numbService = numbService,
            userStatsService = userStatsService,
            userWordsSqlService = userWordsSqlService
        )
    }

    @Provides
    fun playingWordService(
        timeService: TimeService,
        wordsSqlService: WordsSqlService,
        wordOfDayService: WordOfDayService,
        userWordsSqlService: UserWordsSqlService,
        gameUtilService: GameUtilsService,
    ): PlayingWordService {
        return PlayingWordService(
            timeService = timeService,
            wordsSqlService = wordsSqlService,
            gameUtilService = gameUtilService,
            userWordsSqlService = userWordsSqlService,
            wordOfDayService = wordOfDayService
        )
    }

    @Provides
    fun userStatsService(
        userWordsSqlService: UserWordsSqlService,
        statsSqlService: StatsSqlService
    ): UserStatsService {
        return UserStatsService(
            userWordsSqlService = userWordsSqlService,
            statsSqlService = statsSqlService,
        )
    }
}