package com.luisma.game.ui.utils

import com.luisma.core.models.WDayOfWeek
import com.luisma.core_ui.R

fun dayOfWeekUIMapper(day: WDayOfWeek): Int {
    return when (day) {
        WDayOfWeek.Mon -> R.string.mon
        WDayOfWeek.Tue -> R.string.tue
        WDayOfWeek.Wed -> R.string.wed
        WDayOfWeek.Thu -> R.string.thu
        WDayOfWeek.Fri -> R.string.fri
        WDayOfWeek.Sat -> R.string.sat
        WDayOfWeek.Sun -> R.string.sun
    }
}

fun monthUIMapper(month: Int): Int {
    return when (month) {
        1 -> R.string.jan
        2 -> R.string.feb
        3 -> R.string.mar
        4 -> R.string.apr
        5 -> R.string.may
        6 -> R.string.jun
        7 -> R.string.jul
        8 -> R.string.aug
        9 -> R.string.sep
        10 -> R.string.oct
        11 -> R.string.nov
        12 -> R.string.dec
        else -> 0
    }
}