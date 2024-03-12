package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.compose.runtime.Immutable
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction

@Immutable
data class ViewRecipeScreenState(
    val selectedTab: ViewRecipeScreenTab = ViewRecipeScreenTab.INGREDIENTS,
    val isChangeNameDialogOpen: Boolean = false,
    val isDeleteConfirmationDialogOpen: Boolean = false,
    val isRecipeDeleted: Boolean = false,
    val isEditing: Boolean = false,
    val recipeName: String = "",
    val isRecipeStarred: Boolean = false,
    val recipeIngredients: List<Ingredient> = emptyList(),
    val recipeInstructions: List<Instruction> = emptyList()
)