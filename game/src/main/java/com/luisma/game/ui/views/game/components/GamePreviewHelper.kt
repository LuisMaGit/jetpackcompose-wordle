package com.luisma.game.ui.views.game.components

import com.luisma.game.models.ListCharsWithState
import com.luisma.game.models.WChar
import com.luisma.game.models.WCharState
import com.luisma.game.models.WKeyboard
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

private val gridMockList0 = ListCharsWithState(
    done = false,
    chars = persistentListOf(
        WChar(WCharState.RightPlace, 'A'),
        WChar(WCharState.WrongPlace, 'A'),
        WChar(WCharState.RightPlace, 'C'),
        WChar(WCharState.WrongPlace, 'C'),
        WChar(WCharState.RightPlace, 'E'),
    )
)
private val gridMockList1 = ListCharsWithState(
    done = false,
    chars = persistentListOf(
        WChar(WCharState.RightPlace, 'A'),
        WChar(WCharState.WrongPlace, 'A'),
        WChar(WCharState.NoMatch, 'F'),
        WChar(WCharState.NoMatch, 'X'),
        WChar(WCharState.NoMatch, 'X'),
    )
)
private val gridMockList2 = ListCharsWithState(
    done = false,
    chars = persistentListOf(
        WChar(WCharState.Playing, 'A'),
        WChar(WCharState.Playing, 'A'),
        WChar(WCharState.Playing, 'X'),
        WChar(WCharState.Playing, 'F'),
        WChar.boxEmpty()
    )
)

internal val gridMock = persistentMapOf(
    0 to gridMockList0,
    1 to gridMockList1,
    2 to gridMockList0,
    3 to gridMockList0,
    4 to gridMockList1,
    5 to gridMockList2,
)

internal val keyboardMock = WKeyboard.initial()