package com.luisma.core

import android.app.Application
import com.luisma.core.services.NumbService
import com.luisma.core.services.TimeService
import com.luisma.core.services.TimeServiceNowProvider
import com.luisma.core.services.db_services.DbSqlService
import com.luisma.core.services.db_services.StatsSqlService
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.core.services.db_services.WordsSqlService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {
    @Provides
    fun now(): LocalDateTime {
        return LocalDateTime.now()
    }

    @Provides
    fun numbService(): NumbService {
        return NumbService()
    }

    @Provides
    fun timeServiceNowProvider(): TimeServiceNowProvider {
        return TimeServiceNowProvider()
    }

    //2024-03-01T23:10:08
    @Provides
    fun timeService(
        timeServiceNowProvider: TimeServiceNowProvider,
        numbService: NumbService
    ): TimeService {
        return TimeService(
            nowProvider = timeServiceNowProvider,
            numbService = numbService,
        )
    }

    @Provides
    fun dbSqlService(
        app: Application
    ): DbSqlService {
        return DbSqlService(
            context = app
        )
    }

    @Provides
    fun wordsSqlService(
        dbSqlService: DbSqlService
    ): WordsSqlService {
        return WordsSqlService(
            dbSqlService = dbSqlService
        )
    }

    @Provides
    fun userWordsSqlService(
        dbSqlService: DbSqlService
    ): UserWordsSqlService {
        return UserWordsSqlService(
            dbSqlService = dbSqlService
        )
    }

    @Provides
    fun userStatsSqlService(
        dbSqlService: DbSqlService
    ): StatsSqlService {
        return StatsSqlService(
            dbSqlService = dbSqlService
        )
    }

}