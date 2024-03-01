package com.luisma.core.models


data class WTime(
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
        fun noTime(): WTime {
            return WTime(
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
    }
}

enum class WDayOfWeek {
    Mon, Tue, Wed, Thu, Fri, Sat, Sun
}

