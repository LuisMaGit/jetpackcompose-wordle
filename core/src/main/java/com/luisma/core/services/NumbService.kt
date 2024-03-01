package com.luisma.core.services

import java.time.LocalTime
import kotlin.math.absoluteValue
import kotlin.random.Random

class NumbService {
    fun nextPositiveIntUnit(value: Int): Int {
        return Random(LocalTime.now().second * 1000)
            .nextInt(
                from = 0,
                until = value
            )
    }

    fun putPrefixZeroInOneDigitNumber(value: Int): String {
        if (value.absoluteValue.toString().length == 1) {
            return "0$value"
        }

        return value.toString()
    }
}