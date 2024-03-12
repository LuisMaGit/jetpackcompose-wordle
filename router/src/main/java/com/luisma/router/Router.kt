package com.luisma.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luisma.core.services.RouterService
import com.luisma.core.services.Routes
import com.luisma.game.ui.views.game.GameViewBuilder
import com.luisma.game.ui.views.historic.HistoricViewBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun Router(
    routerService: RouterService
) {
    val navController = rememberNavController()
    val navigationCoroutine = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.Game.routeName,
    ) {
        navigationCoroutine.launch {
            routerService.router.collectLatest { routePayload ->
                when (routePayload.route) {
                    Routes.Game -> navController.navigate(route = routePayload.route.routeName)
                    Routes.Historic -> navController.navigate(route = routePayload.route.routeName)
                    Routes.Back -> navController.popBackStack()
                    Routes.GameHistoric -> navController.navigate(
                        route = "${routePayload.route.routeName}/${routePayload.payload}"
                    )
                }
            }
        }

        composable(route = Routes.Game.routeName) {
            GameViewBuilder()
        }

        composable(route = Routes.Historic.routeName) {
            HistoricViewBuilder()
        }

        composable(
            route = "${Routes.GameHistoric.routeName}/{${Routes.GameHistoric.payloadName}}",
            arguments = listOf(navArgument(Routes.GameHistoric.payloadName!!) {
                type = NavType.StringType
            })
        ) {
             GameViewBuilder()
        }
    }
}