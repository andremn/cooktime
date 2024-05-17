package com.nicoapps.cooktime.ui

import android.net.Uri
import androidx.navigation.NavHostController
import com.nicoapps.cooktime.model.Recipe
import com.nicoapps.cooktime.ui.AllDestinations.EXECUTE_RECIPE
import com.nicoapps.cooktime.ui.AllDestinations.HOME
import com.nicoapps.cooktime.ui.AllDestinations.NEW_RECIPE
import com.nicoapps.cooktime.ui.AllDestinations.SETTINGS
import com.nicoapps.cooktime.ui.AllDestinations.VIEW_RECIPE

object AllDestinations {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val NEW_RECIPE = "newRecipe"
    const val VIEW_RECIPE = "viewRecipe"
    const val EXECUTE_RECIPE = "executeRecipe"
}

class AppNavigationActions(
    private val navController: NavHostController
) {

    fun navigateToHome() {
        navController.navigate(HOME) {
            popUpTo(HOME)
        }
    }

    fun navigateToSettings() {
        navController.navigate(SETTINGS) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToNewRecipe() {
        navController.navigate(NEW_RECIPE) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToViewRecipe(recipe: Recipe) {
        val route = "$VIEW_RECIPE/${recipe.id}?recipeTitle=${Uri.encode(recipe.name)}"

        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToExecuteRecipe(recipeId: Long) {
        val route = "$EXECUTE_RECIPE/${recipeId}"

        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}