package com.nicoapps.cooktime.ui.screens.recipe.create.steps

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGrid
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGridMode
import com.nicoapps.cooktime.ui.screens.recipe.draft.DraftIngredient

@Composable
fun NewRecipeIngredientsStep(
    modifier: Modifier = Modifier,
    ingredients: List<DraftIngredient>,
    onIngredientAdded: (String, Float, String?) -> Unit,
    onIngredientUpdated: (Long, String, Float, String?) -> Unit,
    onIngredientRemoved: (Long) -> Unit
) {
    RecipeIngredientsGrid(
        modifier = modifier,
        ingredients = ingredients,
        mode = RecipeIngredientsGridMode.EDIT,
        onIngredientAdded = { name, quantity, measurementUnit ->
            onIngredientAdded(name, quantity, measurementUnit)
        },
        onIngredientUpdated = { key, name, quantity, measurementUnit ->
            onIngredientUpdated(key, name, quantity, measurementUnit)
        },
        onIngredientRemoved = { onIngredientRemoved(it) }
    )
}
