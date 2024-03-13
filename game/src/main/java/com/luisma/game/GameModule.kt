package com.luisma.game

import com.luisma.core.services.PaginationService
import com.luisma.core.services.TimeService
import com.luisma.core.services.db_services.StatsSqlService
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.core.services.db_services.WordsSqlService
import com.luisma.game.services.GameUtilsService
import com.luisma.game.services.PlayingWordService
import com.luisma.game.services.UserHistoricService
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
        userStatsService: UserStatsService,
        userWordsSqlService: UserWordsSqlService
    ): WordOfDayService {
        return WordOfDayService(
            timeService = timeService,
            wordsSqlService = wordsSqlService,
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

    @Provides
    fun userHistoricService(
        userWordsSqlService: UserWordsSqlService,
        paginationService: PaginationService,
        gameUtilsService: GameUtilsService,
        timeService: TimeService,
        wordsSqlService: WordsSqlService
    ): UserHistoricService {
        return UserHistoricService(
            userWordsSqlService = userWordsSqlService,
            paginationService = paginationService,
            gameUtilsService = gameUtilsService,
            timeService = timeService,
            wordSqlService = wordsSqlService
        )
    }
}