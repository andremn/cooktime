package com.nicoapps.cooktime.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nicoapps.cooktime.ui.components.AppBottomBar
import com.nicoapps.cooktime.ui.components.AppDrawer
import com.nicoapps.cooktime.ui.components.AppNavHost
import com.nicoapps.cooktime.ui.components.AppTopBar
import com.nicoapps.cooktime.ui.components.rememberAppSnackbarState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    val currentNavBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: AllDestinations.HOME
    val navigationActions = remember(navHostController) { AppNavigationActions(navHostController) }

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                route = currentRoute,
                navigateToHome = { navigationActions.navigateToHome() },
                navigateToSettings = { navigationActions.navigateToSettings() },
                closeDrawer = { coroutineScope.launch { drawerState.close() } },
                modifier = Modifier
            )
        },
        drawerState = drawerState
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val appSnackbarState = rememberAppSnackbarState(snackbarHostState, coroutineScope)
        var appNavGraphState by remember { mutableStateOf(AppNavGraphState()) }
        var isSearchBarActive by remember { mutableStateOf(false) }

        val isFloatingActionButtonVisible =
            appNavGraphState.floatingActionButton.visible && !isSearchBarActive

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = isFloatingActionButtonVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it * 2 }
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it }
                    )
                )
                {
                    appNavGraphState.floatingActionButton.content?.invoke()
                }
            },
            topBar = {
                AppTopBar(
                    modifier = modifier,
                    appNavGraphState = appNavGraphState,
                    drawerState = drawerState,
                    appNavigationActions = navigationActions,
                    coroutineScope = coroutineScope,
                    onSearchBarActiveChanged = { isSearchBarActive = it }
                )
            },
            bottomBar = {
                AppBottomBar(
                    modifier = modifier,
                    appNavGraphState = appNavGraphState
                )
            },
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets
                .only(sides = WindowInsetsSides.Bottom),
            modifier = modifier
        ) { paddingValues ->
            AppNavHost(
                modifier = modifier,
                paddingValues = paddingValues,
                navController = navHostController,
                appSnackbarState = appSnackbarState,
                navigationActions = navigationActions,
                onDestinationComposing = { appNavGraphState = it }
            )
        }
    }
}