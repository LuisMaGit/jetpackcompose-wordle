package com.luisma.core_ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.luisma.core_ui.R
import com.luisma.core_ui.theme.WBorderRadiusContract
import com.luisma.core_ui.theme.WSpacing
import com.luisma.core_ui.theme.WTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WBottomSheet(
    modifier: Modifier = Modifier,
    show: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(key1 = show) {
        if (!show && sheetState.isVisible) {
            sheetState.hide()
        }

        if (show && !sheetState.isVisible) {
            sheetState.expand()
        }
    }

    if (show)
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = { onDismissRequest() },
            dragHandle = { Box {} },
            containerColor = WTheme.colors.background,
            shape = RoundedCornerShape(
                topStart = WBorderRadiusContract.k16,
                topEnd = WBorderRadiusContract.k16
            )
        ) {
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WSpacing.k18)
            ) {
                WIconButton(
                    id = R.drawable.ic_close,
                    onTap = {
                        coroutine.launch {
                            sheetState.hide()
                            onDismissRequest()
                        }
                    }
                )
            }
            content()
            Spacer(modifier = Modifier.padding(bottom = WSpacing.k80))
        }
}