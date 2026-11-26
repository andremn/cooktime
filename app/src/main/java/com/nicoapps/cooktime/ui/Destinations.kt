package com.nicoapps.cooktime.ui

import kotlinx.serialization.Serializable

/**
 * Destinations of the app's NavHost, as typed Navigation routes. A missing or mistyped argument is
 * a compile-time error instead of a route string that never matches.
 */
@Serializable
data object Home

@Serializable
data object Settings

@Serializable
data object NewRecipe

/**
 * [recipeTitle] is the recipe name known when navigating, so the screen can show it, and animate
 * it as a shared element, before the recipe loads.
 */
@Serializable
data class ViewRecipe(
    val recipeId: Long,
    val recipeTitle: String = ""
)

/** [recipeTitle] follows the same rule as [ViewRecipe.recipeTitle]. */
@Serializable
data class ExecuteRecipe(
    val recipeId: Long,
    val recipeTitle: String = ""
)
