package com.luisma.game.services

import com.luisma.game.models.DEFAULT_WORD_LENGTH
import com.luisma.game.models.GameCursorPosition
import com.luisma.game.models.GameEnabledKeyState
import com.luisma.game.models.KeyboardState
import com.luisma.game.models.LETTERS_WORDS_SEPARATOR
import com.luisma.game.models.LettersRows
import com.luisma.game.models.ListCharsWithState
import com.luisma.game.models.MAX_NUMBER_OF_TRIES_TO_GUESS
import com.luisma.game.models.PlayingWord
import com.luisma.game.models.PlayingWordGameState
import com.luisma.game.models.WCHAR_STATE_NOT_RELATED_TO_GUESSED_WORD
import com.luisma.game.models.WChar
import com.luisma.game.models.WCharAnimationState
import com.luisma.game.models.WCharState
import com.luisma.game.models.WKeyboard
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentSet

class GameUtilsService {

    /**
     * checks the state of each char [WCharState] in the word [wordsWithSeparators],
     * been [toGuessWord] to word to guess,
     * if the word is guessed [ListCharsWithState.done] = true,
     *
     * e.g.
     * [wordsWithSeparators] = "AACCE,AAF", [toGuessWord] = "ABCDE",
     * ```
     * result = mapOf(
     *  0 to ListCharsWithState(
     *             done = false,
     *             chars = listOf(
     *                 WChar(WCharState.RightPlace, 'A'),
     *                 WChar(WCharState.WrongPlace, 'A'),
     *                 WChar(WCharState.RightPlace, 'C'),
     *                 WChar(WCharState.WrongPlace, 'C'),
     *                 WChar(WCharState.RightPlace, 'E'),
     *             )
     *         ),
     *  1 to ListCharsWithState(
     *             done = false,
     *             chars = listOf(
     *                 WChar(WCharState.RightPlace, 'A'),
     *                 WChar(WCharState.WrongPlace, 'A'),
     *                 WChar(WCharState.Def, 'F'),
     *                 WChar.empty(),
     *                 WChar.empty(),
     *             )
     * )
     * .
     * .
     * .
     *  5 to ListCharsWithState(
     *             done = false,
     *             chars = listOf(
     *                 WChar.empty(),
     *                 WChar.empty(),
     *                 WChar.empty(),
     *                 WChar.empty(),
     *                 WChar.empty(),
     *             )
     * )
     * ```
     */
    fun resolveLettersState(
        wordsWithSeparators: String,
        toGuessWord: String
    ): LettersRows {
        val lettersList = wordsWithSeparators.split(Regex(LETTERS_WORDS_SEPARATOR.toString()))
        val output: MutableMap<Int, ListCharsWithState> = mutableMapOf()

        if (lettersList.any { it.isEmpty() }) {
            output += mapOf(
                0 to getFullRowEmpty()
            )
        } else {
            lettersList.forEachIndexed { idxSavedWord, savedWord ->
                val lettersListModel = mutableListOf<WChar>()
                // fill the places with chars
                savedWord.forEachIndexed { idxCharSavedWord, charSavedWord ->
                    val wChar = if (toGuessWord[idxCharSavedWord] == charSavedWord) {
                        WChar(
                            state = WCharState.RightPlace,
                            char = charSavedWord
                        )
                    } else if (toGuessWord.contains(charSavedWord)) {
                        WChar(
                            state = WCharState.WrongPlace,
                            char = charSavedWord
                        )
                    } else {
                        WChar(
                            state = WCharState.NoMatch,
                            char = charSavedWord
                        )
                    }
                    lettersListModel.add(wChar)
                }

                // fill the empty spaces
                if (lettersListModel.count() < DEFAULT_WORD_LENGTH) {
                    for (x in lettersListModel.count() until DEFAULT_WORD_LENGTH) {
                        lettersListModel.add(WChar.boxEmpty())
                    }
                }

                // add to the map
                if (savedWord.isNotBlank()) {
                    output += mapOf(
                        idxSavedWord to ListCharsWithState(
                            done = lettersListModel.all { it.state == WCharState.RightPlace },
                            chars = lettersListModel.toImmutableList(),
                        )
                    )
                }
            }
        }


        // fill map empty spaces
        val lastKey = output.entries.last().key
        if (lastKey < MAX_NUMBER_OF_TRIES_TO_GUESS) {
            for (x in lastKey + 1 until MAX_NUMBER_OF_TRIES_TO_GUESS) {
                output += mapOf(
                    x to getFullRowEmpty()
                )
            }
        }

        return output.toImmutableMap()
    }

    /**
     * returns the entire keyboard [WKeyboard] with the state
     * resulting from the word formed by [lettersRows]
     */
    fun resolveFullKeyboard(
        lettersRows: LettersRows
    ): WKeyboard {
        val allLetters = lettersRows.values
            .map { it.chars }
            .flatten()
            .filter { !WCHAR_STATE_NOT_RELATED_TO_GUESSED_WORD.contains(it.state) }
        val initialKeyboard = WKeyboard.initial()
        return WKeyboard(
            row1 = resolveKeyboardRow(
                keyboardRow = initialKeyboard.row1,
                allLetters = allLetters,
            ), row2 = resolveKeyboardRow(
                keyboardRow = initialKeyboard.row2,
                allLetters = allLetters,
            ), row3 = resolveKeyboardRow(
                keyboardRow = initialKeyboard.row3,
                allLetters = allLetters,
            )
        )
    }

    /**
     * returns the provided keyboard row [KeyboardState]
     * resulting from the word formed by [allLetters]
     */
    fun resolveKeyboardRow(
        keyboardRow: KeyboardState,
        allLetters: List<WChar>
    ): KeyboardState {
        val keyboardRowOutput = mutableSetOf<WChar>()
        for (key in keyboardRow) {
            val matches = allLetters.filter { it.char == key.char }
            // is not in this row
            if (matches.isEmpty()) {
                keyboardRowOutput += key
                continue
            }

            // priority 1 - right place
            if (matches.any { it.state == WCharState.RightPlace }) {
                keyboardRowOutput += key.copy(state = WCharState.RightPlace)
                continue
            }

            // priority 2 - wrong place
            if (matches.any { it.state == WCharState.WrongPlace }) {
                keyboardRowOutput += key.copy(state = WCharState.WrongPlace)
                continue
            }

            //  priority 3 - no match
            if (matches.any { it.state == WCharState.NoMatch }) {
                keyboardRowOutput += key.copy(state = WCharState.NoMatch)
            }
        }
        return keyboardRowOutput.toPersistentSet()
    }

    /**
     * returns if the last played row is:
     * * [PlayingWordGameState.Win] -> the last played row is done
     * * [PlayingWordGameState.Incomplete] -> not reached the [MAX_NUMBER_OF_TRIES_TO_GUESS] and is not done,
     * * [PlayingWordGameState.Lose] -> last played row reached the [MAX_NUMBER_OF_TRIES_TO_GUESS] and is not done,
     */
    fun getPlayingWordGameState(
        lettersRow: LettersRows,
    ): PlayingWordGameState {
        var output = PlayingWordGameState.Incomplete
        for (entry in lettersRow) {
            if (entry.value.done) {
                output = PlayingWordGameState.Win
                break
            }
            //incomplete
            if (entry.value.chars.any { WCHAR_STATE_NOT_RELATED_TO_GUESSED_WORD.contains(it.state) }) {
                break
            }

            if (entry.key == MAX_NUMBER_OF_TRIES_TO_GUESS - 1 &&
                !entry.value.done
            ) {
                output = PlayingWordGameState.Lose
                break
            }
        }
        return output
    }

    /**
     * returns the first occurrence of [WChar.boxEmpty] position,
     * or if all the chars are in a row are [WCharState.Playing]
     * the last of this
     */
    fun getCursorPosition(
        lettersRow: LettersRows
    ): GameCursorPosition {
        var output = GameCursorPosition.initial()
        for (entry in lettersRow) {
            val allChars = entry.value.chars
            // edge case: all box filled while playing
            if (allChars.all { it.state == WCharState.Playing }) {
                output = GameCursorPosition(
                    row = entry.key,
                    column = allChars.count() - 1,
                )
                break
            }

            val firstEmptyIdx = allChars.indexOfFirst {
                it.state == WCharState.Empty
            }
            if (firstEmptyIdx != -1) {
                output = GameCursorPosition(
                    row = entry.key,
                    column = firstEmptyIdx,
                )
                break
            }
        }
        return output
    }

    /**
     * should be called when tapping the delete button on the keyboard
     * it assumes that the columns would be the only affected
     * */
    fun removeLastChar(
        playingRow: List<Char>,
        lettersRow: LettersRows,
    ): GameUtilsAddRemoveCharResponse {

        val cursorPosition = getCursorPosition(
            lettersRow = lettersRow
        )
        val newColumn = cursorPosition.column - 1
        val newCursorPosition = cursorPosition.copy(
            column = if (newColumn < 0) 0 else newColumn
        )

        val mutablePlayingRow = playingRow.map { it }.toMutableList()
        mutablePlayingRow.removeLast()

        val mutableLetterRows = lettersRow.toMutableMap()
        mutableLetterRows[cursorPosition.row] = getListCharsWithStateFromListChars(
            chars = mutablePlayingRow,
            putLastAsAppearAnimation = false
        )

        return GameUtilsAddRemoveCharResponse(
            enabledKeyState = getEnabledKeyStateFromPlayingRow(mutablePlayingRow),
            listChars = mutablePlayingRow,
            cursorPosition = newCursorPosition,
            letterRows = mutableLetterRows.toImmutableMap()
        )
    }

    /**
     * should be called when tapping a letter on the keyboard
     */
    fun addChar(
        playingRow: List<Char>,
        lettersRow: LettersRows,
        newChar: Char,
    ): GameUtilsAddRemoveCharResponse {

        val cursorPosition = getCursorPosition(
            lettersRow = lettersRow
        )
        val newColumn = cursorPosition.column + 1
        val lastIdx = DEFAULT_WORD_LENGTH - 1
        val newCursorPosition = cursorPosition.copy(
            column = if (newColumn >= lastIdx) lastIdx else newColumn
        )

        val mutablePlayingRow = playingRow.map { it }.toMutableList()
        mutablePlayingRow.add(newChar)

        val mutableLetterRows = lettersRow.toMutableMap()
        mutableLetterRows[cursorPosition.row] = getListCharsWithStateFromListChars(
            chars = mutablePlayingRow,
            putLastAsAppearAnimation = true
        )

        return GameUtilsAddRemoveCharResponse(
            enabledKeyState = getEnabledKeyStateFromPlayingRow(mutablePlayingRow),
            listChars = mutablePlayingRow,
            cursorPosition = newCursorPosition,
            letterRows = mutableLetterRows.toImmutableMap()
        )
    }

    /**
     * joins the [lettersRows] chars in a String with words separated
     * by [LETTERS_WORDS_SEPARATOR], except the last element
     * e.g.
     * * ABCDE
     * * ABCDE,FGHIJ
     */
    fun lettersRowsToStringWithSeparator(
        lettersRows: LettersRows
    ): String {
        var output = ""
        val validLettersRows = mutableMapOf<Int, ListCharsWithState>()
        for (entry in lettersRows) {
            val validRow = entry.value.chars.filter { wChar ->
                wChar.state != WCharState.Empty
            }
            if (validRow.isEmpty()) {
                break
            }
            validLettersRows += mapOf(
                entry.key to entry.value.copy(
                    chars = validRow.toImmutableList()
                )
            )
        }
        for ((index, entry) in validLettersRows) {
            val newWord = entry.chars
                .map { it.char.toString() }
                .reduce { acc, c -> acc + c }

            val isLast = index == validLettersRows.entries.count() - 1
            output += if (isLast) newWord else newWord + LETTERS_WORDS_SEPARATOR
        }

        return output
    }

    /**
     * will show the successful guessed word
     * only when the user lose
     */
    fun getCorrectWordWithState(
        playingWord: PlayingWord
    ): ListCharsWithState {
        if (playingWord.state == PlayingWordGameState.Lose)
            return resolveLettersState(
                toGuessWord = playingWord.word,
                wordsWithSeparators = playingWord.word
            ).entries.first().value

        return ListCharsWithState.empty()
    }

    fun getFullRowEmpty(): ListCharsWithState {
        return ListCharsWithState(
            done = false,
            chars = List(size = DEFAULT_WORD_LENGTH) { WChar.boxEmpty() }.toImmutableList()
        )
    }

    fun wordFromWCharsToString(chars: List<Char>): String {
        var output = ""
        chars.forEach { char ->
            output += char.toString()
        }
        return output
    }

    fun getListCharsWithStateFromListChars(
        chars: List<Char>,
        putLastAsAppearAnimation: Boolean,
    ): ListCharsWithState {
        val wChars = chars.mapIndexed { idx, char ->
            WChar(
                char = char,
                state = WCharState.Playing,
                animationState = if (putLastAsAppearAnimation && idx == chars.count() - 1) {
                    WCharAnimationState.Appear
                } else {
                    WCharAnimationState.Still
                }
            )
        }.toMutableList()

        if (chars.count() < DEFAULT_WORD_LENGTH) {
            for (x in chars.count() until DEFAULT_WORD_LENGTH) {
                wChars.add(WChar.boxEmpty())
            }
        }

        return ListCharsWithState(
            done = false,
            chars = wChars.toImmutableList()
        )
    }

    fun getEnabledKeyStateFromPlayingRow(
        listChars: List<Char>
    ): GameEnabledKeyState {
        return GameEnabledKeyState(
            enabledEnter = listChars.count() == DEFAULT_WORD_LENGTH,
            enableAdd = listChars.count() < DEFAULT_WORD_LENGTH,
            enabledDelete = listChars.isNotEmpty()
        )
    }

    fun putAnimationStateToRow(
        rowIdx: Int,
        lettersRow: LettersRows,
        animationState: WCharAnimationState
    ): LettersRows {
        val mutableLetterRows = lettersRow.toMutableMap()
        val mutableChars = mutableLetterRows[rowIdx]!!.chars.toMutableList()
        mutableLetterRows[rowIdx] = mutableLetterRows[rowIdx]!!.copy(
            chars = mutableChars.map {
                it.copy(
                    animationState = animationState
                )
            }.toImmutableList()
        )
        return mutableLetterRows.toImmutableMap()
    }
}

data class GameUtilsAddRemoveCharResponse(
    val listChars: List<Char>,
    val enabledKeyState: GameEnabledKeyState,
    val cursorPosition: GameCursorPosition,
    val letterRows: LettersRows,
)








