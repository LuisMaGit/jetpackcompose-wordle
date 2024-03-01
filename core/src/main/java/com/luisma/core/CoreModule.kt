package com.luisma.core

import android.app.Application
import com.luisma.core.services.NumbService
import com.luisma.core.services.TimeService
import com.luisma.core.services.TimeServiceNowProvider
import com.luisma.core.services.WordsSqlService
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
    fun wordsSqlService(
        app: Application
    ): WordsSqlService {
        return WordsSqlService(
            context = app
        )
    }

}