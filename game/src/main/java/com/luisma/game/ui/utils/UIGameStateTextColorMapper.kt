package com.luisma.game.ui.utils

import androidx.compose.ui.graphics.Color
import com.luisma.core.models.db.UserWordsPlayingStateContract
import com.luisma.core_ui.theme.WColors

fun gameStateTextColorUIMapper(
    state: UserWordsPlayingStateContract,
    wColors: WColors,
): Color {
    return when (state) {
        UserWordsPlayingStateContract.PLAYING -> wColors.placeholderFill
        UserWordsPlayingStateContract.LOSE -> wColors.placeholderBorderBold
        UserWordsPlayingStateContract.SOLVED_NOT_IN_TIME -> wColors.placeholderOrange
        UserWordsPlayingStateContract.SOLVED_IN_TIME -> wColors.placeholderGreen
    }
}