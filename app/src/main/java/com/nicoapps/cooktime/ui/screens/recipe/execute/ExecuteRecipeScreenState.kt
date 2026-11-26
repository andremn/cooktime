package com.nicoapps.cooktime.ui.screens.recipe.execute

import androidx.annotation.StringRes
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction

data class ExecuteRecipeScreenState(
    val recipeId: Long = 0,
    val recipeName: String = "",
    val ingredients: List<IngredientState> = emptyList(),
    val instructions: List<InstructionState> = emptyList(),
    val isSaveExecutionDialogOpen: Boolean = false,
    val isSaving: Boolean = false,
    val isExecutionSaved: Boolean = false,
    @StringRes val errorMessageResourceId: Int? = null
)

data class IngredientState(
    val ingredient: Ingredient,
    val isChecked: Boolean
)

data class InstructionState(
    val instruction: Instruction,
    val isChecked: Boolean
)
