package com.nicoapps.cooktime.ui.screens.recipe.execute

import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction

data class ExecuteRecipeScreenState(
    val recipeName: String = "",
    val ingredients: List<IngredientState> = emptyList(),
    val instructions: List<InstructionState> = emptyList(),
    val isSaveExecutionDialogOpen: Boolean = false
)

data class IngredientState(
    val ingredient: Ingredient,
    val isChecked: Boolean
)

data class InstructionState(
    val instruction: Instruction,
    val isChecked: Boolean
)