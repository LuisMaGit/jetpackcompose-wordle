package com.luisma.core.services

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class RouterService {
    private val _router = MutableSharedFlow<RoutePayload>()
    val router: SharedFlow<RoutePayload> = _router

    suspend fun goTo(route: RoutePayload) {
        _router.emit(route)
    }

    suspend fun goBack() {
        _router.emit(RoutePayload(route = Routes.Back))
    }
}

sealed class Routes(
    val routeName: String,
    val payloadName: String? = null
) {
    object Back : Routes(routeName = "back")

    object Game : Routes(routeName = "game")

    object Historic : Routes(routeName = "historic")

    object GameHistoric : Routes(
        routeName = "game_historic",
        payloadName = "word_id"
    )
}

data class RoutePayload(
    val route: Routes,
    val payload: String? = null
)