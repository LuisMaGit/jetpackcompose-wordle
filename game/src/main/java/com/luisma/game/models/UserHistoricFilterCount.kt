package com.luisma.game.models

import javax.annotation.concurrent.Immutable


@Immutable
data class UserHistoricFilterCount (
    val solvedOnTime: Int,
    val solvedNotOnTime : Int,
    val playing: Int,
    val lost: Int
)