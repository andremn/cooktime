package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nicoapps.cooktime.ui.AllDestinations
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.defaultAnimationSpec
import com.nicoapps.cooktime.ui.screens.home.HomeScreen
import com.nicoapps.cooktime.ui.screens.recipe.NewRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.ViewRecipeScreen
import com.nicoapps.cooktime.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
    appSnackbarState: AppSnackbarState,
    navigationActions: AppNavigationActions,
    onDestinationComposing: (AppNavGraphState) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AllDestinations.HOME,
        modifier = modifier
            .consumeWindowInsets(paddingValues)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal
                )
            ),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = defaultAnimationSpec()
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = defaultAnimationSpec()
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = defaultAnimationSpec()
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = defaultAnimationSpec()
            )
        }
    ) {

        composable(AllDestinations.HOME) {
            HomeScreen(
                modifier = modifier.padding(paddingValues),
                appNavigationActions = navigationActions,
                onComposing = { onDestinationComposing(it) }
            )
        }

        composable(AllDestinations.SETTINGS) {
            SettingsScreen(
                onComposing = { onDestinationComposing(it) }
            )
        }

        composable(AllDestinations.NEW_RECIPE) {
            NewRecipeScreen(
                modifier = modifier
                    .padding(paddingValues),
                appSnackbarState = appSnackbarState,
                onComposing = { onDestinationComposing(it) },
                appNavigationActions = navigationActions
            )
        }

        composable(
            route = "${AllDestinations.VIEW_RECIPE}/{recipeId}?recipeTitle={recipeTitle}",
            arguments = listOf(
                navArgument("recipeId") { type = NavType.IntType },
                navArgument("recipeTitle") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            ViewRecipeScreen(
                modifier = modifier.padding(paddingValues),
                onComposing = { onDestinationComposing(it) }
            )
        }
    }
}