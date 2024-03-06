package com.luisma.game.models

import com.luisma.core.models.WDuration
import javax.annotation.concurrent.Immutable

@Immutable
data class NextWordDuration(
    val hours: Int,
    val min: Int,
    val sec: Int,
    val hoursStr: String,
    val minStr: String,
    val secStr: String,
) {
    companion object {
        fun zero(): NextWordDuration {
            return NextWordDuration(
                hours = 0,
                min = 0,
                sec = 0,
                hoursStr = "00",
                minStr = "00",
                secStr = "00"
            )
        }

        fun fromWDuration(wDuration: WDuration): NextWordDuration {
            return NextWordDuration(
                hours = wDuration.hours,
                min = wDuration.min,
                sec = wDuration.sec,
                hoursStr = wDuration.hoursStr,
                minStr = wDuration.minStr,
                secStr = wDuration.secStr
            )
        }
    }
}