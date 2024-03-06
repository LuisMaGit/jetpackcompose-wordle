package com.luisma.core_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun WThemeProvider(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
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