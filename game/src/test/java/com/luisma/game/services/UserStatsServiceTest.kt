package com.luisma.game.services

import com.luisma.core.services.db_services.StatsSqlService
import com.luisma.core.services.db_services.UserWordsSqlService
import com.luisma.game.models.GameUserStatsWinDistribution
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UserStatsServiceTest {

    private fun getService(
        userWordsSqlService: UserWordsSqlService = mockk(),
        statsSqlService: StatsSqlService = mockk()
    ): UserStatsService {
        return UserStatsService(
            userWordsSqlService = userWordsSqlService,
            statsSqlService = statsSqlService
        )
    }

    @Test
    fun `winDistributionFromStrToInt - tests`() {
        assertEquals(
            getService().winDistributionFromStrToInt("1,2,3,35"),
            listOf(1, 2, 3, 35)
        )

        assertEquals(
            getService().winDistributionFromStrToInt("1,2,3,A"),
            listOf(1, 2, 3, 0)
        )
    }

    @Test
    fun `winDistributionFromIntToStr - test`() {
        assertEquals(
            getService().winDistributionFromIntToStr(listOf(1, 2, 3, 35)),
            "1,2,3,35"
        )
    }

    @Test
    fun `getWinDistributionWithMaxAsReference - test`() {
        assertEquals(
            getService().getWinDistributionWithMaxAsReference(listOf(1, 2, 3, 35)),
            listOf(
                GameUserStatsWinDistribution(
                    value = 1,
                    percentageWithMaxAsReference = 0.028571429f
                ),
                GameUserStatsWinDistribution(
                    value = 2,
                    percentageWithMaxAsReference = 0.057142857f
                ),
                GameUserStatsWinDistribution(
                    value = 3,
                    percentageWithMaxAsReference = 0.08571429f
                ),
                GameUserStatsWinDistribution(
                    value = 35,
                    percentageWithMaxAsReference = 1f
                )
            )
        )
    }
}