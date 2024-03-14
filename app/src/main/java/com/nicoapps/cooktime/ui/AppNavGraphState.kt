package com.nicoapps.cooktime.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

enum class AppNavGraphTopBarContentType {
    NONE,
    TITLE_ONLY,
    SEARCH_BAR
}

data class AppNavGraphState(
    val topBar: AppNavGraphTopBarState = AppNavGraphTopBarState(),
    val bottomBar: AppNavGraphBottomBarState = AppNavGraphBottomBarState(),
    val floatingActionButton: AppNavGraphFloatingActionButtonState =
        AppNavGraphFloatingActionButtonState()
)

data class AppNavGraphTopBarState(
    val contentType: AppNavGraphTopBarContentType = AppNavGraphTopBarContentType.NONE,
    val showActions: Boolean = false,
    val title: (@Composable () -> Unit)? = null,
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val searchBarContent: (@Composable () -> Unit)? = null
)

data class AppNavGraphBottomBarState(
    val visible: Boolean = false,
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val floatingActionButton: (@Composable () -> Unit)? = null
)

data class AppNavGraphFloatingActionButtonState(
    val visible: Boolean = false,
    val content: (@Composable () -> Unit)? = null
)