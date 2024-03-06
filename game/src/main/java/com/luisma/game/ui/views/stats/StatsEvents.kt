package com.luisma.game.ui.views.stats

sealed class StatsEvents {
    object InitStats : StatsEvents()
    object DisposeStats : StatsEvents()
}