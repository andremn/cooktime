package com.nicoapps.cooktime.ui.screens.recipe.steps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGrid
import com.nicoapps.cooktime.ui.components.recipe.RecipeIngredientsGridMode

@Composable
fun NewRecipeIngredientsStep(
    modifier: Modifier = Modifier,
    ingredients: List<Ingredient>,
    onCurrentTitleChanged: (String) -> Unit,
    onIngredientAdded: (Ingredient) -> Unit,
    onIngredientUpdated: (Int, Ingredient) -> Unit,
    onIngredientRemoved: (Ingredient) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onCurrentTitleChanged(
            context.resources.getString(R.string.new_recipe_ingredients_step_title)
        )
    }

    RecipeIngredientsGrid(
        modifier = modifier,
        ingredients = ingredients,
        mode = RecipeIngredientsGridMode.EDIT,
        onIngredientAdded = { onIngredientAdded(it) },
        onIngredientUpdated = { index, ingredient ->
            onIngredientUpdated(index, ingredient)
        },
        onIngredientRemoved = { onIngredientRemoved(it) }
    )
}