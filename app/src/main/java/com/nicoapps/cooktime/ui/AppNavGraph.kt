package com.nicoapps.cooktime.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nicoapps.cooktime.ui.components.AppDrawer
import com.nicoapps.cooktime.ui.components.AppDrawerItem
import com.nicoapps.cooktime.ui.components.AppNavHost
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
    val currentDestination = currentNavBackStackEntry?.destination
    val selectedDrawerItem = when {
        currentDestination?.hasRoute<Settings>() == true -> AppDrawerItem.SETTINGS
        currentDestination?.hasRoute<Home>() == true -> AppDrawerItem.HOME
        else -> null
    }

    // Living above the NavHost keeps a snackbar shown right before a destination change visible
    // on the destination the user lands on.
    val snackbarHostState = remember { SnackbarHostState() }
    val appSnackbarState = rememberAppSnackbarState(snackbarHostState, coroutineScope)

    ModalNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            AppDrawer(
                selectedItem = selectedDrawerItem,
                navigateToHome = {
                    navHostController.navigate(Home) {
                        popUpTo<Home>()
                        launchSingleTop = true
                    }
                },
                navigateToSettings = {
                    navHostController.navigate(Settings) {
                        launchSingleTop = true
                    }
                },
                closeDrawer = { coroutineScope.launch { drawerState.close() } },
                modifier = Modifier
            )
        },
        drawerState = drawerState
    ) {
        AppNavHost(
            navController = navHostController,
            appSnackbarState = appSnackbarState,
            onOpenDrawer = { coroutineScope.launch { drawerState.open() } }
        )
    }
}
