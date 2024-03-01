package com.luisma.core.services

import com.luisma.core.models.WDayOfWeek
import com.luisma.core.models.WDuration
import com.luisma.core.models.WTime
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeService(
    private val nowProvider: TimeServiceNowProvider,
    private val numbService: NumbService,
) {

    private val now: LocalDateTime
        get() = nowProvider.now()

    private val mapDayOfWeek = mapOf(
        DayOfWeek.MONDAY to WDayOfWeek.Mon,
        DayOfWeek.TUESDAY to WDayOfWeek.Tue,
        DayOfWeek.WEDNESDAY to WDayOfWeek.Wed,
        DayOfWeek.THURSDAY to WDayOfWeek.Thu,
        DayOfWeek.FRIDAY to WDayOfWeek.Fri,
        DayOfWeek.SATURDAY to WDayOfWeek.Sat,
        DayOfWeek.SUNDAY to WDayOfWeek.Sun,
    )

    private fun getDateOnly(value: LocalDateTime): LocalDate {
        return value.toLocalDate()
    }

    private fun isToday(value: LocalDateTime): Boolean {
        return value.year == now.year
                && value.month == now.month
                && value.dayOfMonth == now.dayOfMonth
    }

    private fun fromLocalDateTimeToWTime(value: LocalDateTime): WTime {
        return WTime(
            year = value.year,
            month = value.monthValue,
            dayOfMonth = value.dayOfMonth,
            hour = value.hour,
            min = value.minute,
            sec = value.second,
            dayOfWeek = mapDayOfWeek[value.dayOfWeek]!!,
            isToday = isToday(value),
            isYesterday = isYesterday(value),
            isInTheLastWeek = isInTheLastWeek(value),
        )
    }

    private fun fromWTimeTimeToLocalDateTime(value: WTime): LocalDateTime {
        return LocalDateTime.of(
            value.year,
            value.month,
            value.dayOfMonth,
            value.hour,
            value.min,
            value.sec
        )
    }

    private fun fromStrToLocalDateTime(value: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(value)
        } catch (e: Exception) {
            null
        }
    }

    private fun fromJavaDurationToWDuration(duration: Duration): WDuration {
        val seconds = duration.seconds
        val hours = (seconds.floorDiv(3600)).toInt()
        val hoursRest = (seconds % 3600).toInt()
        val min = (hoursRest / 60).toInt()
        val sec = (hoursRest % 60).toInt()


        return WDuration(
            hours = hours,
            min = min,
            sec = sec,
            hoursStr = numbService.putPrefixZeroInOneDigitNumber(hours),
            minStr = numbService.putPrefixZeroInOneDigitNumber(min),
            secStr = numbService.putPrefixZeroInOneDigitNumber(sec),
        )
    }

    private fun plus24Hours(dateTime: LocalDateTime): LocalDateTime {
        return dateTime.plusMinutes(1)
    }

    /**
     * FORMAT: 2007-12-03T10:15:30
     */
    fun fromStrToWTime(value: String): WTime? {
        val dateTime = fromStrToLocalDateTime(value) ?: return null
        return fromLocalDateTimeToWTime(dateTime)
    }

    fun fromWTimeToStr(value: WTime): String {
        return LocalDateTime.of(
            value.year,
            value.month,
            value.dayOfMonth,
            value.hour,
            value.min,
            value.sec
        ).format(DateTimeFormatter.ISO_DATE_TIME)
    }

    fun getWTimeNow(): WTime {
        return fromLocalDateTimeToWTime(now)
    }

    fun isYesterday(value: LocalDateTime): Boolean {
        return getDateOnly(value) == getDateOnly(now).minusDays(1)
    }

    fun isInTheLastWeek(value: LocalDateTime): Boolean {
        val nowDate = getDateOnly(now)
        val valueDate = getDateOnly(value)
        return nowDate.minusWeeks(1).isBefore(valueDate)
                && nowDate.isAfter(valueDate)
    }

    fun itHasNotBeen24HoursSince(time: WTime): Boolean {
        val dateTime = fromWTimeTimeToLocalDateTime(time)
        return now.isAfter(dateTime) && now.isBefore(plus24Hours(dateTime))
    }

    fun itHasNotBeen24HoursSince(localDateTime: LocalDateTime): Boolean {
        return itHasNotBeen24HoursSince(
            fromLocalDateTimeToWTime(localDateTime)
        )
    }

    fun timePlus24Hours(time: WTime): WTime {
        val dateTime = fromWTimeTimeToLocalDateTime(time)
        return fromLocalDateTimeToWTime(plus24Hours(dateTime))
    }

    fun durationFromNowUntil(until: WTime): WDuration {
        val dateTime = fromWTimeTimeToLocalDateTime(until)

        if (dateTime.isBefore(now)) {
            return WDuration.zero()
        }

        return fromJavaDurationToWDuration(
            Duration.between(now, dateTime)
        )
    }

    fun durationMinus1Sec(duration: WDuration): WDuration {
        if (duration == WDuration.zero()) {
            return duration
        }
        val seconds = duration.hours * 3600 + duration.min * 60 + duration.sec
        return fromJavaDurationToWDuration(
            Duration.ofSeconds(seconds.toLong()).minusSeconds(1)
        )
    }
}

class TimeServiceNowProvider {
    fun now(): LocalDateTime {
        return LocalDateTime.now().withNano(0)
    }
}















