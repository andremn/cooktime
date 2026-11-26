package com.nicoapps.cooktime.ui

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import androidx.navigation.toRoute
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The route arguments reach a [SavedStateHandle] and come back intact through `toRoute`, the same
 * round trip the destination ViewModels go through. It runs on a device because reading route
 * arguments relies on `Bundle`.
 */
class NavigationArgumentsTest {
    private val specialRecipeTitle = "Pão / queijo? 100% & #1"

    @Test
    fun viewRecipeArgumentsSurviveTheRoute() {
        val savedStateHandle = SavedStateHandle(
            route = ViewRecipe(recipeId = RECIPE_ID, recipeTitle = specialRecipeTitle)
        )

        val destination = savedStateHandle.toRoute<ViewRecipe>()

        assertEquals(RECIPE_ID, destination.recipeId)
        assertEquals(specialRecipeTitle, destination.recipeTitle)
    }

    @Test
    fun executeRecipeArgumentsSurviveTheRoute() {
        val savedStateHandle = SavedStateHandle(
            route = ExecuteRecipe(recipeId = RECIPE_ID, recipeTitle = specialRecipeTitle)
        )

        val destination = savedStateHandle.toRoute<ExecuteRecipe>()

        assertEquals(RECIPE_ID, destination.recipeId)
        assertEquals(specialRecipeTitle, destination.recipeTitle)
    }

    @Test
    fun recipeTitleDefaultsToEmptyWhenItIsNotInTheRoute() {
        val savedStateHandle = SavedStateHandle(route = ViewRecipe(recipeId = RECIPE_ID))

        val destination = savedStateHandle.toRoute<ViewRecipe>()

        assertEquals(RECIPE_ID, destination.recipeId)
        assertEquals("", destination.recipeTitle)
    }

    private companion object {
        private const val RECIPE_ID = 42L
    }
}
