package com.nicoapps.cooktime.ui.screens.recipe.create.steps

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.components.recipe.RecipeInstructionsColumn
import com.nicoapps.cooktime.ui.screens.recipe.draft.DraftInstruction

@Composable
fun NewRecipeInstructionsStep(
    modifier: Modifier = Modifier,
    instructions: List<DraftInstruction>,
    onInstructionAdded: (String) -> Unit,
    onInstructionUpdated: (Long, String) -> Unit,
    onInstructionRemoved: (Long) -> Unit
) {
    RecipeInstructionsColumn(
        modifier = modifier,
        isEditing = true,
        instructions = instructions,
        onInstructionAdded = { onInstructionAdded(it) },
        onInstructionUpdated = { key, text ->
            onInstructionUpdated(key, text)
        },
        onInstructionRemoved = { onInstructionRemoved(it) }
    )
}
