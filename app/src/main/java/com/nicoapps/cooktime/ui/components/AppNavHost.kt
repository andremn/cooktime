package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nicoapps.cooktime.ui.AllDestinations
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.defaultAnimationSpec
import com.nicoapps.cooktime.ui.screens.recipe.create.NewRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.edit.ViewRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.execute.ExecuteRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.list.HomeScreen
import com.nicoapps.cooktime.ui.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appSnackbarState: AppSnackbarState,
    navigationActions: AppNavigationActions,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AllDestinations.HOME,
        modifier = modifier,
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
                appNavigationActions = navigationActions,
                appSnackbarState = appSnackbarState,
                onOpenDrawer = onOpenDrawer
            )
        }

        composable(AllDestinations.SETTINGS) {
            SettingsScreen()
        }

        composable(AllDestinations.NEW_RECIPE) {
            NewRecipeScreen(
                appSnackbarState = appSnackbarState,
                appNavigationActions = navigationActions
            )
        }

        composable(
            route = "${AllDestinations.VIEW_RECIPE}/{recipeId}?recipeTitle={recipeTitle}",
            arguments = listOf(
                navArgument("recipeId") { type = NavType.LongType },
                navArgument("recipeTitle") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            ViewRecipeScreen(
                snackbarState = appSnackbarState,
                appNavigationActions = navigationActions
            )
        }

        composable(
            route = "${AllDestinations.EXECUTE_RECIPE}/{recipeId}",
            arguments = listOf(
                navArgument("recipeId") { type = NavType.LongType }
            )
        ) {
            ExecuteRecipeScreen(
                appNavigationActions = navigationActions
            )
        }
    }
}
