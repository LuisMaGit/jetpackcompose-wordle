package com.luisma.game.models

import com.luisma.core.models.WDayOfWeek
import com.luisma.core.models.WTime
import javax.annotation.concurrent.Immutable

@Immutable
data class PlayingWordDate(
    val year: Int,
    //1-12
    val month: Int,
    val dayOfMonth: Int,
    val dayOfWeek: WDayOfWeek,
    val hour: Int,
    val min: Int,
    val sec: Int,
    val isToday: Boolean,
    val isYesterday: Boolean,
    val isInTheLastWeek: Boolean
) {
    companion object {
        fun noTime(): PlayingWordDate {
            return PlayingWordDate(
                year = 0,
                month = 0,
                dayOfMonth = 0,
                dayOfWeek = WDayOfWeek.Sun,
                hour = 0,
                min = 0,
                sec = 0,
                isToday = false,
                isYesterday = false,
                isInTheLastWeek = false,
            )
        }

        fun fromWTime(wTime: WTime): PlayingWordDate {
            return PlayingWordDate(
                year = wTime.year,
                month = wTime.month,
                dayOfMonth = wTime.dayOfMonth,
                dayOfWeek = wTime.dayOfWeek,
                hour = wTime.hour,
                min = wTime.min,
                sec = wTime.sec,
                isToday = wTime.isToday,
                isYesterday = wTime.isYesterday,
                isInTheLastWeek = wTime.isInTheLastWeek,
            )
        }
    }
}