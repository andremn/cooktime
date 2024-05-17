package com.nicoapps.cooktime.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class AppSnackbarState(
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun show(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onDismissed: (CoroutineScope.(SnackbarResult) -> Unit)? = null
    ) {
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )

            onDismissed?.invoke(coroutineScope, result)
        }
    }
}

@Composable
fun rememberAppSnackbarState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, coroutineScope) {
        AppSnackbarState(
            snackbarHostState,
            coroutineScope
        )
    }