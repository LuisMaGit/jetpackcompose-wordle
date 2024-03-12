package com.luisma.game.ui.views.stats

sealed class StatsViewEvents {
    object InitStats : StatsViewEvents()
    object DisposeStats : StatsViewEvents()
}