package com.luisma.game.models

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import javax.annotation.concurrent.Immutable

@Immutable
data class WKeyboard(
    val row1: KeyboardState,
    val row2: KeyboardState,
    val row3: KeyboardState,
) {
    companion object {
        fun initial(): WKeyboard {
            return WKeyboard(
                row1 = persistentSetOf(
                    WChar.keyboardEmpty('Z'),
                    WChar.keyboardEmpty('X'),
                    WChar.keyboardEmpty('C'),
                    WChar.keyboardEmpty('V'),
                    WChar.keyboardEmpty('B'),
                    WChar.keyboardEmpty('N'),
                    WChar.keyboardEmpty('M'),
                ),
                row2 = persistentSetOf(
                    WChar.keyboardEmpty('A'),
                    WChar.keyboardEmpty('S'),
                    WChar.keyboardEmpty('D'),
                    WChar.keyboardEmpty('F'),
                    WChar.keyboardEmpty('G'),
                    WChar.keyboardEmpty('H'),
                    WChar.keyboardEmpty('J'),
                    WChar.keyboardEmpty('K'),
                    WChar.keyboardEmpty('L'),
                ),
                row3 = persistentSetOf(
                    WChar.keyboardEmpty('Q'),
                    WChar.keyboardEmpty('W'),
                    WChar.keyboardEmpty('E'),
                    WChar.keyboardEmpty('R'),
                    WChar.keyboardEmpty('T'),
                    WChar.keyboardEmpty('Y'),
                    WChar.keyboardEmpty('U'),
                    WChar.keyboardEmpty('I'),
                    WChar.keyboardEmpty('O'),
                    WChar.keyboardEmpty('P'),
                )
            )
        }
    }

}

typealias KeyboardState = ImmutableSet<WChar>