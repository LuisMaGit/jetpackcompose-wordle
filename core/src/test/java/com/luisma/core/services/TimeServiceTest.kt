package com.luisma.core.services

import com.luisma.core.models.WDayOfWeek
import com.luisma.core.models.WDuration
import com.luisma.core.models.WTime
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class TimeServiceTest {

    private val defNow = LocalDateTime.of(
        2022,
        12,
        31,
        10,
        15,
        30
    )

    private fun getNowProvider(now: LocalDateTime): TimeServiceNowProvider {
        val mock = mockk<TimeServiceNowProvider>()
        every {
            mock.now()
        } returns now
        return mock
    }

    private fun getTimeService(
        nowProvider: TimeServiceNowProvider = getNowProvider(defNow),
        numbService: NumbService = NumbService()
    ): TimeService {
        return TimeService(
            nowProvider,
            numbService
        )
    }

    @Test
    fun `isYesterday - success yesterday`() {
        val now = defNow.withMonth(1).withDayOfMonth(1)
        val yesterday = now.withYear(2021).withMonth(12).withDayOfMonth(31)
        val result = getTimeService(getNowProvider(now)).isYesterday(yesterday)
        assertTrue(result)
    }

    @Test
    fun `isYesterday - fail yesterday`() {
        val now = defNow.withMonth(1).withDayOfMonth(1)
        val yesterday = now.withYear(2021).withMonth(12).withDayOfMonth(30)
        val result = getTimeService(getNowProvider(now)).isYesterday(yesterday)
        assertFalse(result)
    }

    @Test
    fun `isInTheLastWeek - day in last week`() {
        val lw = defNow.withDayOfMonth(25)
        val result = getTimeService().isInTheLastWeek(lw)
        assertTrue(result)
    }

    @Test
    fun `formWTimeToStr - should return 2007 12 03T 10 15 30 in ISO_DATE_TIME`() {
        val expected = "2007-12-03T10:15:30"
        val wTime = getTimeService().fromStrToWTime(expected)
        val result = getTimeService().fromWTimeToStr(wTime!!)
        assertEquals(expected, result)
    }

    @Test
    fun `itHasNotBeen24HoursSince - test some cases`() {
        val now = LocalDateTime.of(
            2023,
            1,
            15,
            10,
            30,
        )
        assertTrue(
            getTimeService(getNowProvider(now)).itHasNotBeen24HoursSince(
                now.withDayOfMonth(14).withHour(11)
            )
        )
        assertTrue(
            getTimeService(getNowProvider(now)).itHasNotBeen24HoursSince(
                now.withDayOfMonth(14).withHour(10).withMinute(31)
            )
        )

        assertFalse(
            getTimeService(getNowProvider(now)).itHasNotBeen24HoursSince(
                now.withDayOfMonth(14).withHour(10).withMinute(30)
            )
        )
        assertFalse(
            getTimeService(getNowProvider(now)).itHasNotBeen24HoursSince(
                now.withDayOfMonth(14).withHour(9).withMinute(30)
            )
        )
    }


    @Test
    fun `timePlus24Hours - test1`() {
        assertEquals(
            getTimeService().timePlus24Hours(
                WTime.noTime().copy(
                    year = 2023,
                    month = 1,
                    dayOfMonth = 16,
                    hour = 0,
                    min = 0,
                    sec = 1,
                )
            ),
            WTime.noTime().copy(
                year = 2023,
                month = 1,
                dayOfMonth = 17,
                hour = 0,
                min = 0,
                sec = 1,
                dayOfWeek = WDayOfWeek.Tue
            )
        )
    }

    @Test
    fun `durationFromNowUntil - date is before now`() {
        val now = LocalDateTime.of(
            2023,
            1,
            15,
            10,
            30,
            0,
        )

        val time = WTime(
            year = 2023,
            month = 1,
            dayOfMonth = 15,
            dayOfWeek = WDayOfWeek.Sun,
            hour = 10,
            min = 29,
            sec = 0,
            isToday = false,
            isYesterday = false,
            isInTheLastWeek = false,
        )

        assertEquals(
            getTimeService(getNowProvider(now)).durationFromNowUntil(
                time
            ),
            WDuration.zero()
        )
    }

    @Test
    fun `durationFromNowUntil - test 1`() {
        val now = LocalDateTime.of(
            2023,
            1,
            15,
            10,
            30,
            0,
        )

        val time = WTime(
            year = 2023,
            month = 1,
            dayOfMonth = 17,
            dayOfWeek = WDayOfWeek.Sun,
            hour = 11,
            min = 34,
            sec = 30,
            isToday = false,
            isYesterday = false,
            isInTheLastWeek = false,
        )

        assertEquals(
            getTimeService(getNowProvider(now)).durationFromNowUntil(
                time
            ),
            WDuration(
                hours = 49,
                min = 4,
                sec = 30,
                hoursStr = "49",
                minStr = "04",
                secStr = "30"
            )
        )
    }

    @Test
    fun `durationFromNowUntil - test 2`() {
        val now = LocalDateTime.of(
            2023,
            1,
            15,
            10,
            30,
            27,
        )

        val time = WTime(
            year = 2023,
            month = 1,
            dayOfMonth = 15,
            dayOfWeek = WDayOfWeek.Sun,
            hour = 11,
            min = 34,
            sec = 30,
            isToday = false,
            isYesterday = false,
            isInTheLastWeek = false,
        )

        assertEquals(
            getTimeService(getNowProvider(now)).durationFromNowUntil(
                time
            ),
            WDuration(
                hours = 1,
                min = 4,
                sec = 3,
                hoursStr = "01",
                minStr = "04",
                secStr = "03"
            )
        )
    }

    @Test
    fun `durationFromNowUntil - test 3`() {
        val now = LocalDateTime.of(
            2023,
            1,
            15,
            10,
            30,
            30,
        )

        val time = WTime(
            year = 2023,
            month = 1,
            dayOfMonth = 15,
            dayOfWeek = WDayOfWeek.Sun,
            hour = 10,
            min = 34,
            sec = 30,
            isToday = false,
            isYesterday = false,
            isInTheLastWeek = false,
        )

        assertEquals(
            getTimeService(getNowProvider(now)).durationFromNowUntil(
                time
            ),
            WDuration(
                hours = 0,
                min = 4,
                sec = 0,
                hoursStr = "00",
                minStr = "04",
                secStr = "00"
            )
        )
    }

    @Test
    fun `durationFromNowUntil - test 4`() {
        val now = LocalDateTime.of(
            2023,
            1,
            15,
            10,
            30,
            30,
        )

        val time = WTime(
            year = 2023,
            month = 1,
            dayOfMonth = 16,
            dayOfWeek = WDayOfWeek.Sun,
            hour = 10,
            min = 30,
            sec = 29,
            isToday = false,
            isYesterday = false,
            isInTheLastWeek = false,
        )

        assertEquals(
            getTimeService(getNowProvider(now)).durationFromNowUntil(
                time
            ),
            WDuration(
                hours = 23,
                min = 59,
                sec = 59,
                hoursStr = "23",
                minStr = "59",
                secStr = "59"
            )
        )
    }

    @Test
    fun `durationMinus1Sec - test 1`() {
        val input = WDuration(
            hours = 24,
            min = 0,
            sec = 0,
            hoursStr = "24",
            minStr = "00",
            secStr = "00"
        )
        val output = WDuration(
            hours = 23,
            min = 59,
            sec = 59,
            hoursStr = "23",
            minStr = "59",
            secStr = "59"
        )
        assertEquals(
            getTimeService().durationMinus1Sec(input),
            output,
        )
    }

    @Test
    fun `durationMinus1Sec - test 2`() {
        val input = WDuration(
            hours = 0,
            min = 0,
            sec = 0,
            hoursStr = "00",
            minStr = "00",
            secStr = "00"
        )
        val output = WDuration.zero()
        assertEquals(
            getTimeService().durationMinus1Sec(input),
            output,
        )
    }

    @Test
    fun `durationMinus1Sec - test 3`() {
        val input = WDuration(
            hours = 0,
            min = 2,
            sec = 0,
            hoursStr = "00",
            minStr = "02",
            secStr = "00"
        )
        val output = WDuration(
            hours = 0,
            min = 1,
            sec = 59,
            hoursStr = "00",
            minStr = "01",
            secStr = "59"
        )
        assertEquals(
            getTimeService().durationMinus1Sec(input),
            output,
        )
    }
}