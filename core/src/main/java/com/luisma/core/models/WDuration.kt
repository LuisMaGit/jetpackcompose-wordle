package com.luisma.core.models

data class WDuration(
    val hours: Int,
    val min: Int,
    val sec: Int,
    val hoursStr : String,
    val minStr : String,
    val secStr : String,
) {
    companion object {
        fun zero(): WDuration {
            return WDuration(
                hours = 0,
                min = 0,
                sec = 0,
                hoursStr = "00",
                minStr = "00",
                secStr = "00"
            )
        }
    }
}