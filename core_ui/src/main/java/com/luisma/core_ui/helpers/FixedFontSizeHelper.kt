package com.luisma.core_ui.helpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp

val Int.fontSizeNonScaledSp
    @Composable
    get() = (this / LocalDensity.current.fontScale).sp