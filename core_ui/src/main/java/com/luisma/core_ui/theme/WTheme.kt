package com.luisma.core_ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier

@Composable
fun WTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        WColors.dark()
    } else {
        WColors.light()
    }

    CompositionLocalProvider(
        LocalWColors provides colors,
    ) {
        content()
    }
}


@Composable
fun WThemeProvider(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit,
) {
    WTheme(
        darkTheme = darkTheme ?: isSystemInDarkTheme()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WTheme.colors.background),
        ) {
            content()
        }
    }
}

/**
 * Contains functions to access the current theme values
 **/
object WTheme {
    val colors: WColors
        @Composable
        @ReadOnlyComposable
        get() = LocalWColors.current

    val typography: WType
        @Composable
        @ReadOnlyComposable
        get() = LocalWTheme.current
}