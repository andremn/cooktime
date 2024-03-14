package com.nicoapps.cooktime.ui.screens.recipe.edit.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGrid
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGridMode

@Composable
fun ViewRecipeIngredientsTab(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    ingredients: List<Ingredient>,
    onIngredientAdded: (Ingredient) -> Unit,
    onIngredientUpdated: (Int, Ingredient) -> Unit,
    onIngredientRemoved: (Ingredient) -> Unit
) {
    RecipeIngredientsGrid(
        modifier = modifier,
        ingredients = ingredients,
        mode = RecipeIngredientsGridMode.fromIsEditing(isEditing),
        onIngredientAdded = { onIngredientAdded(it) },
        onIngredientUpdated = { index, ingredient -> onIngredientUpdated(index, ingredient) },
        onIngredientRemoved = { onIngredientRemoved(it) }
    )
}