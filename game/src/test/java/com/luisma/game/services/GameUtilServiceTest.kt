package com.luisma.game.services

import com.luisma.game.models.GameCursorPosition
import com.luisma.game.models.GameEnabledKeyState
import com.luisma.game.models.ListCharsWithState
import com.luisma.game.models.PlayingWordGameState
import com.luisma.game.models.WChar
import com.luisma.game.models.WCharAnimationState
import com.luisma.game.models.WCharState
import com.luisma.game.models.WKeyboard
import junit.framework.TestCase.assertEquals
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import org.junit.Test

class GameUtilServiceTest {

    private fun getService(): GameUtilsService {
        return GameUtilsService()
    }

    private fun fullRowEmpty(): ListCharsWithState {
        return getService().getFullRowEmpty()
    }

    @Test
    fun `resolveLettersState - test 1`() {
        val word = "ABCDE"
        val wordsWithSeparators = "AACCE"
        val list0 = ListCharsWithState(
            done = false,
            chars = persistentListOf(
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.WrongPlace, 'A'),
                WChar(WCharState.RightPlace, 'C'),
                WChar(WCharState.WrongPlace, 'C'),
                WChar(WCharState.RightPlace, 'E'),
            )
        )
        assertEquals(
            getService().resolveLettersState(wordsWithSeparators, word),
            mapOf(
                0 to list0,
                1 to fullRowEmpty(),
                2 to fullRowEmpty(),
                3 to fullRowEmpty(),
                4 to fullRowEmpty(),
                5 to fullRowEmpty()
            )
        )
    }

    @Test
    fun `resolveLettersState - test 2`() {
        val word = "ABCDE"
        val wordsWithSeparators = "AACCE,AAF"
        val list0 = ListCharsWithState(
            done = false,
            chars = persistentListOf(
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.WrongPlace, 'A'),
                WChar(WCharState.RightPlace, 'C'),
                WChar(WCharState.WrongPlace, 'C'),
                WChar(WCharState.RightPlace, 'E'),
            )
        )
        val list1 = ListCharsWithState(
            done = false,
            chars = persistentListOf(
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.WrongPlace, 'A'),
                WChar(WCharState.NoMatch, 'F'),
                WChar.boxEmpty(),
                WChar.boxEmpty(),
            )
        )
        assertEquals(
            getService().resolveLettersState(wordsWithSeparators, word),
            mapOf(
                0 to list0,
                1 to list1,
                2 to fullRowEmpty(),
                3 to fullRowEmpty(),
                4 to fullRowEmpty(),
                5 to fullRowEmpty(),
            ),
        )
    }

    @Test
    fun `resolveLettersState - test 3`() {
        val word = "ABCAE"
        val wordsWithSeparators = "AACAS,AAFXC,ABCAE,R"
        val list0 = ListCharsWithState(
            done = false,
            chars = persistentListOf(
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.WrongPlace, 'A'),
                WChar(WCharState.RightPlace, 'C'),
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.NoMatch, 'S'),
            )
        )
        val list1 = ListCharsWithState(
            done = false,
            chars = persistentListOf(
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.WrongPlace, 'A'),
                WChar(WCharState.NoMatch, 'F'),
                WChar(WCharState.NoMatch, 'X'),
                WChar(WCharState.WrongPlace, 'C'),
            )
        )
        val list2 = ListCharsWithState(
            done = true,
            chars = persistentListOf(
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.RightPlace, 'B'),
                WChar(WCharState.RightPlace, 'C'),
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.RightPlace, 'E'),
            )
        )
        val list3 = ListCharsWithState(
            done = false,
            chars = persistentListOf(
                WChar(WCharState.NoMatch, 'R'),
                WChar.boxEmpty(),
                WChar.boxEmpty(),
                WChar.boxEmpty(),
                WChar.boxEmpty(),
            )
        )
        assertEquals(
            getService().resolveLettersState(wordsWithSeparators, word),
            mapOf(
                0 to list0,
                1 to list1,
                2 to list2,
                3 to list3,
                4 to fullRowEmpty(),
                5 to fullRowEmpty(),
            ),
        )
    }

    @Test
    fun `resolveLettersState - test 4`() {
        val word = "ABCDE"
        val wordsWithSeparators = ","
        assertEquals(
            getService().resolveLettersState(wordsWithSeparators, word),
            mapOf(
                0 to fullRowEmpty(),
                1 to fullRowEmpty(),
                2 to fullRowEmpty(),
                3 to fullRowEmpty(),
                4 to fullRowEmpty(),
                5 to fullRowEmpty(),
            ),
        )
    }

    @Test
    fun `wordFromWChars - success`() {
        val list0 = listOf(
            'A',
            'A',
            'C',
            'A',
            'S',
        )
        assertEquals(
            getService().wordFromWCharsToString(list0), "AACAS"
        )
    }

    @Test
    fun `resolveKeyboardRow - row 1`() {
        val allLetters = listOf(
            WChar(WCharState.NoMatch, 'Z'),
            WChar(WCharState.WrongPlace, 'X'),
            WChar(WCharState.NoMatch, 'C'),
            WChar(WCharState.RightPlace, 'V'),
            WChar(WCharState.NoMatch, 'B'),
            WChar(WCharState.WrongPlace, 'N'),
            WChar(WCharState.RightPlace, 'M'),
            WChar(WCharState.RightPlace, 'Z'),
            WChar(WCharState.WrongPlace, 'X'),
            WChar(WCharState.RightPlace, 'V'),
            WChar(WCharState.NoMatch, 'B'),
            WChar(WCharState.WrongPlace, 'N'),
            WChar(WCharState.RightPlace, 'M'),
        )

        val result = setOf(
            WChar(WCharState.RightPlace, 'Z'),
            WChar(WCharState.WrongPlace, 'X'),
            WChar(WCharState.NoMatch, 'C'),
            WChar(WCharState.RightPlace, 'V'),
            WChar(WCharState.NoMatch, 'B'),
            WChar(WCharState.WrongPlace, 'N'),
            WChar(WCharState.RightPlace, 'M'),
        )

        assertEquals(
            getService().resolveKeyboardRow(
                keyboardRow = WKeyboard.initial().row1,
                allLetters = allLetters
            ),
            result,
        )
    }

    @Test
    fun `resolveKeyboardRow - row 2`() {
        val allLetters = listOf(
            WChar(WCharState.WrongPlace, 'A'),
            WChar(WCharState.RightPlace, 'S'),
            WChar(WCharState.NoMatch, 'D'),
            WChar(WCharState.NoMatch, 'S'),
            WChar(WCharState.NoMatch, 'G'),
            WChar(WCharState.RightPlace, 'A'),
            WChar(WCharState.NoMatch, 'A'),
            WChar(WCharState.WrongPlace, 'G'),
            WChar(WCharState.NoMatch, 'D'),
            WChar(WCharState.RightPlace, 'G'),
            WChar(WCharState.NoMatch, 'F'),
            WChar(WCharState.WrongPlace, 'F'),
        )

        val result = setOf(
            WChar(WCharState.RightPlace, 'A'),
            WChar(WCharState.RightPlace, 'S'),
            WChar(WCharState.NoMatch, 'D'),
            WChar(WCharState.WrongPlace, 'F'),
            WChar(WCharState.RightPlace, 'G'),
            WChar.keyboardEmpty('H'),
            WChar.keyboardEmpty('J'),
            WChar.keyboardEmpty('K'),
            WChar.keyboardEmpty('L'),
        )

        assertEquals(
            getService().resolveKeyboardRow(
                keyboardRow = WKeyboard.initial().row2,
                allLetters = allLetters
            ),
            result,
        )
    }

    @Test
    fun `resolveFullKeyboard - success`() {
        val lettersRows = persistentMapOf(
            0 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.Empty, 'Q'),
                    WChar(WCharState.Empty, 'W'),
                    WChar(WCharState.Empty, 'E'),
                    WChar(WCharState.Empty, 'R'),
                    WChar(WCharState.Empty, 'T'),
                    WChar(WCharState.Empty, 'Y'),
                    WChar(WCharState.Empty, 'U'),
                    WChar(WCharState.Empty, 'I'),
                    WChar(WCharState.Empty, 'O'),
                    WChar(WCharState.Empty, 'P'),
                )
            ),
            1 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.NoMatch, 'Z'),
                    WChar(WCharState.WrongPlace, 'X'),
                    WChar(WCharState.NoMatch, 'C'),
                    WChar(WCharState.RightPlace, 'V'),
                    WChar(WCharState.NoMatch, 'B'),
                    WChar(WCharState.WrongPlace, 'N'),
                    WChar(WCharState.RightPlace, 'M'),
                    WChar(WCharState.RightPlace, 'Z'),
                    WChar(WCharState.WrongPlace, 'X'),
                    WChar(WCharState.RightPlace, 'V'),
                    WChar(WCharState.NoMatch, 'B'),
                    WChar(WCharState.WrongPlace, 'N'),
                    WChar(WCharState.RightPlace, 'M'),
                )
            ),
            2 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'S'),
                    WChar(WCharState.NoMatch, 'D'),
                    WChar(WCharState.NoMatch, 'S'),
                    WChar(WCharState.NoMatch, 'G'),
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.NoMatch, 'A'),
                    WChar(WCharState.WrongPlace, 'G'),
                    WChar(WCharState.NoMatch, 'D'),
                    WChar(WCharState.RightPlace, 'G'),
                    WChar(WCharState.NoMatch, 'F'),
                    WChar(WCharState.WrongPlace, 'F'),
                )
            )
        )

        val result = WKeyboard(
            row1 = persistentSetOf(
                WChar(WCharState.RightPlace, 'Z'),
                WChar(WCharState.WrongPlace, 'X'),
                WChar(WCharState.NoMatch, 'C'),
                WChar(WCharState.RightPlace, 'V'),
                WChar(WCharState.NoMatch, 'B'),
                WChar(WCharState.WrongPlace, 'N'),
                WChar(WCharState.RightPlace, 'M'),
            ),
            row2 = persistentSetOf(
                WChar(WCharState.RightPlace, 'A'),
                WChar(WCharState.RightPlace, 'S'),
                WChar(WCharState.NoMatch, 'D'),
                WChar(WCharState.WrongPlace, 'F'),
                WChar(WCharState.RightPlace, 'G'),
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

        assertEquals(
            getService().resolveFullKeyboard(lettersRows),
            result,
        )
    }

    @Test
    fun `getPlayingWordGameState - test 1`() {
        val input = persistentMapOf(
            0 to ListCharsWithState(
                done = true,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            )
        )

        assertEquals(
            getService().getPlayingWordGameState(input),
            PlayingWordGameState.Win
        )
    }

    @Test
    fun `getPlayingWordGameState - test 2`() {
        val input = persistentMapOf(
            0 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            1 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            2 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            3 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            4 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                )
            ),
            5 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                )
            )
        )

        assertEquals(
            getService().getPlayingWordGameState(input),
            PlayingWordGameState.Lose
        )
    }

    @Test
    fun `getCursorPosition - test`() {
        val input = persistentMapOf(
            0 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            1 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            2 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            3 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'A'),
                    WChar.boxEmpty(),
                    WChar(WCharState.WrongPlace, 'C'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
        )

        assertEquals(
            getService().getCursorPosition(input),
            GameCursorPosition(
                row = 3,
                column = 2
            )
        )
    }

    @Test
    fun `removeLastChar - test only one letter`() {
        assertEquals(
            getService().removeLastChar(
                playingRow = listOf('A'),
                lettersRow = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(char = 'A', state = WCharState.Playing),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                        )
                    )
                )
            ),
            GameUtilsAddRemoveCharResponse(
                enabledKeyState = GameEnabledKeyState(
                    enabledDelete = false,
                    enabledEnter = false,
                    enableAdd = true,
                ),
                listChars = emptyList(),
                cursorPosition = GameCursorPosition(row = 0, column = 0),
                letterRows = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `removeLastChar - test most of the time behaviour`() {
        assertEquals(
            getService().removeLastChar(
                playingRow = listOf('A', 'B', 'C'),
                lettersRow = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(char = 'A', state = WCharState.Playing),
                            WChar(char = 'B', state = WCharState.Playing),
                            WChar(char = 'C', state = WCharState.Playing),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                        )
                    )
                )
            ),
            GameUtilsAddRemoveCharResponse(
                enabledKeyState = GameEnabledKeyState(
                    enabledDelete = true,
                    enabledEnter = false,
                    enableAdd = true,
                ),
                listChars = listOf('A', 'B'),
                cursorPosition = GameCursorPosition(row = 0, column = 2),
                letterRows = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(
                                char = 'A',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'B',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `removeLastChar - test full row`() {
        assertEquals(
            getService().removeLastChar(
                playingRow = listOf('A', 'B', 'C', 'D', 'E'),
                lettersRow = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(char = 'A', state = WCharState.Playing),
                            WChar(char = 'B', state = WCharState.Playing),
                            WChar(char = 'C', state = WCharState.Playing),
                            WChar(char = 'D', state = WCharState.Playing),
                            WChar(char = 'E', state = WCharState.Playing),
                        )
                    )
                )
            ),
            GameUtilsAddRemoveCharResponse(
                enabledKeyState = GameEnabledKeyState(
                    enabledDelete = true,
                    enabledEnter = false,
                    enableAdd = true,
                ),
                listChars = listOf('A', 'B', 'C', 'D'),
                cursorPosition = GameCursorPosition(row = 0, column = 3),
                letterRows = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(
                                char = 'A',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'B',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'C',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'D',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar.boxEmpty(),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `addChar - test empty`() {
        assertEquals(
            getService().addChar(
                newChar = 'A',
                playingRow = emptyList(),
                lettersRow = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                        )
                    )
                )
            ),
            GameUtilsAddRemoveCharResponse(
                enabledKeyState = GameEnabledKeyState(
                    enabledDelete = true,
                    enabledEnter = false,
                    enableAdd = true,
                ),
                listChars = listOf('A'),
                cursorPosition = GameCursorPosition(row = 0, column = 1),
                letterRows = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(
                                char = 'A',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                            WChar.boxEmpty(),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `addChar - almost full`() {
        assertEquals(
            getService().addChar(
                newChar = 'E',
                playingRow = listOf('A', 'B', 'C', 'D'),
                lettersRow = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(char = 'A', state = WCharState.Playing),
                            WChar(char = 'B', state = WCharState.Playing),
                            WChar(char = 'C', state = WCharState.Playing),
                            WChar(char = 'D', state = WCharState.Playing),
                            WChar.boxEmpty(),
                        )
                    )
                )
            ),
            GameUtilsAddRemoveCharResponse(
                enabledKeyState = GameEnabledKeyState(
                    enabledDelete = true,
                    enabledEnter = true,
                    enableAdd = false,
                ),
                listChars = listOf('A', 'B', 'C', 'D', 'E'),
                cursorPosition = GameCursorPosition(row = 0, column = 4),
                letterRows = persistentMapOf(
                    0 to ListCharsWithState(
                        done = false,
                        chars = persistentListOf(
                            WChar(
                                char = 'A',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'B',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'C',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'D',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                            WChar(
                                char = 'E',
                                state = WCharState.Playing,
                                animationState = WCharAnimationState.Appear
                            ),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `getListCharsWithStateFromListChars - test`() {
        assertEquals(
            getService().getListCharsWithStateFromListChars(
                chars = listOf('A', 'B', 'C')
            ),
            ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(
                        state = WCharState.Playing,
                        char = 'A',
                        animationState = WCharAnimationState.Appear
                    ),
                    WChar(
                        state = WCharState.Playing,
                        char = 'B',
                        animationState = WCharAnimationState.Appear
                    ),
                    WChar(
                        state = WCharState.Playing,
                        char = 'C',
                        animationState = WCharAnimationState.Appear
                    ),
                    WChar.boxEmpty(),
                    WChar.boxEmpty()
                )
            )
        )

        assertEquals(
            getService().getListCharsWithStateFromListChars(
                chars = listOf('A', 'B', 'C', 'D', 'E')
            ),
            ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(
                        state = WCharState.Playing,
                        char = 'A',
                        animationState = WCharAnimationState.Appear
                    ),
                    WChar(
                        state = WCharState.Playing,
                        char = 'B',
                        animationState = WCharAnimationState.Appear
                    ),
                    WChar(
                        state = WCharState.Playing,
                        char = 'C',
                        animationState = WCharAnimationState.Appear
                    ),
                    WChar(
                        state = WCharState.Playing,
                        char = 'D',
                        animationState = WCharAnimationState.Appear
                    ),
                    WChar(
                        state = WCharState.Playing,
                        char = 'E',
                        animationState = WCharAnimationState.Appear
                    ),
                )
            )
        )

        assertEquals(
            getService().getListCharsWithStateFromListChars(
                chars = emptyList()
            ),
            ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty()
                )
            )
        )
    }

    @Test
    fun `lettersRowsToStringWithSeparator - only one row`() {
        val input = persistentMapOf(
            0 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'B'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'D'),
                    WChar(WCharState.RightPlace, 'E'),
                ),
            ),
        )

        assertEquals(
            getService().lettersRowsToStringWithSeparator(
                input
            ),
            "ABCDE"
        )
    }

    @Test
    fun `lettersRowsToStringWithSeparator - one row and rest of other`() {
        val input = persistentMapOf(
            0 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'B'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'D'),
                    WChar(WCharState.RightPlace, 'E'),
                ),
            ),
            1 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'F'),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                ),
            ),
            2 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                    WChar.boxEmpty(),
                ),
            ),
        )

        assertEquals(
            getService().lettersRowsToStringWithSeparator(
                input
            ),
            "ABCDE,F"
        )
    }

    @Test
    fun `lettersRowsToStringWithSeparator - several rows`() {
        val input = persistentMapOf(
            0 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'B'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'D'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            1 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'F'),
                    WChar(WCharState.WrongPlace, 'G'),
                    WChar(WCharState.RightPlace, 'H'),
                    WChar(WCharState.WrongPlace, 'I'),
                    WChar(WCharState.RightPlace, 'J'),
                )
            ),
        )

        assertEquals(
            getService().lettersRowsToStringWithSeparator(
                input
            ),
            "ABCDE,FGHIJ"
        )
    }

    @Test
    fun `lettersRowsToStringWithSeparator - several rows 2`() {
        val input = persistentMapOf(
            0 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'A'),
                    WChar(WCharState.WrongPlace, 'B'),
                    WChar(WCharState.RightPlace, 'C'),
                    WChar(WCharState.WrongPlace, 'D'),
                    WChar(WCharState.RightPlace, 'E'),
                )
            ),
            1 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'F'),
                    WChar(WCharState.WrongPlace, 'G'),
                    WChar(WCharState.RightPlace, 'H'),
                    WChar(WCharState.WrongPlace, 'I'),
                    WChar(WCharState.RightPlace, 'J'),
                )
            ),
            2 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'K'),
                    WChar(WCharState.WrongPlace, 'L'),
                    WChar(WCharState.RightPlace, 'M'),
                    WChar(WCharState.WrongPlace, 'N'),
                    WChar(WCharState.RightPlace, 'O'),
                )
            ),
            3 to ListCharsWithState(
                done = false,
                chars = persistentListOf(
                    WChar(WCharState.RightPlace, 'P'),
                    WChar(WCharState.WrongPlace, 'Q'),
                    WChar(WCharState.RightPlace, 'R'),
                    WChar(WCharState.WrongPlace, 'S'),
                    WChar(WCharState.RightPlace, 'T'),
                )
            ),
        )

        assertEquals(
            getService().lettersRowsToStringWithSeparator(
                input
            ),
            "ABCDE,FGHIJ,KLMNO,PQRST"
        )
    }
}


