package com.luisma.core.services

import java.time.LocalTime
import kotlin.math.absoluteValue
import kotlin.random.Random

class NumbService {
    fun putPrefixZeroInOneDigitNumber(value: Int): String {
        if (value.absoluteValue.toString().length == 1) {
            return "0$value"
        }

        return value.toString()
    }
}