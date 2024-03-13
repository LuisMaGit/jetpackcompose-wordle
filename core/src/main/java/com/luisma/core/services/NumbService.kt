package com.luisma.core.services

import kotlin.math.absoluteValue

class NumbService {
    fun putPrefixZeroInOneDigitNumber(value: Int): String {
        if (value.absoluteValue.toString().length == 1) {
            return "0$value"
        }

        return value.toString()
    }
}