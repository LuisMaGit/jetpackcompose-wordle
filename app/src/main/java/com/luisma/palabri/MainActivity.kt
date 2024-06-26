package com.luisma.palabri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.MobileAds
import com.luisma.core.services.AnalyticsService
import com.luisma.core.services.RouterService
import com.luisma.core_ui.theme.WTheme
import com.luisma.core_ui.theme.WThemeProvider
import com.luisma.router.Router
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var routerService: RouterService

    @Inject
    lateinit var analyticsService: AnalyticsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analyticsService.onCreate()
        MobileAds.initialize(this) {}
        setContent {
            WThemeProvider {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WTheme.colors.background
                ) {
                    Router(
                        routerService = routerService
                    )
                }
            }
        }
    }
}

