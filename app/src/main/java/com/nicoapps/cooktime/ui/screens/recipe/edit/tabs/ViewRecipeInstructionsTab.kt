package com.nicoapps.cooktime.ui.screens.recipe.edit.tabs

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.ui.components.recipe.RecipeInstructionsColumn

@Composable
fun ViewRecipeInstructionsTab(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    recipeInstructions: List<Instruction>,
    onInstructionAdded: (Instruction) -> Unit,
    onInstructionUpdated: (Int, Instruction) -> Unit,
    onInstructionRemoved: (Instruction) -> Unit
) {
    RecipeInstructionsColumn(
        modifier = modifier.padding(8.dp),
        isEditing = isEditing,
        instructions = recipeInstructions,
        onInstructionAdded = { onInstructionAdded(it) },
        onInstructionUpdated = { index, instruction ->
            onInstructionUpdated(index, instruction)
        },
        onInstructionRemoved = { onInstructionRemoved(it) }
    )
}