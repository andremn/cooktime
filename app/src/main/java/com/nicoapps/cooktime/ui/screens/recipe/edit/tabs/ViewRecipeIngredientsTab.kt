package com.nicoapps.cooktime.ui.screens.recipe.edit.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGrid
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGridMode
import com.nicoapps.cooktime.ui.screens.recipe.draft.DraftIngredient

@Composable
fun ViewRecipeIngredientsTab(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    ingredients: List<DraftIngredient>,
    onIngredientAdded: (name: String, quantity: Float, measurementUnit: String?) -> Unit,
    onIngredientUpdated: (
        key: Long,
        name: String,
        quantity: Float,
        measurementUnit: String?
    ) -> Unit,
    onIngredientRemoved: (key: Long) -> Unit
) {
    RecipeIngredientsGrid(
        modifier = modifier,
        ingredients = ingredients,
        mode = RecipeIngredientsGridMode.fromIsEditing(isEditing),
        onIngredientAdded = { name, quantity, measurementUnit ->
            onIngredientAdded(name, quantity, measurementUnit)
        },
        onIngredientUpdated = { key, name, quantity, measurementUnit ->
            onIngredientUpdated(key, name, quantity, measurementUnit)
        },
        onIngredientRemoved = { onIngredientRemoved(it) }
    )
}
