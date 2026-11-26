package com.nicoapps.cooktime.ui.screens.recipe.edit.tabs

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.ui.components.recipe.RecipeInstructionsColumn
import com.nicoapps.cooktime.ui.screens.recipe.draft.DraftInstruction

@Composable
fun ViewRecipeInstructionsTab(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    recipeInstructions: List<DraftInstruction>,
    onInstructionAdded: (text: String) -> Unit,
    onInstructionUpdated: (key: Long, text: String) -> Unit,
    onInstructionRemoved: (key: Long) -> Unit
) {
    RecipeInstructionsColumn(
        modifier = modifier.padding(8.dp),
        isEditing = isEditing,
        instructions = recipeInstructions,
        onInstructionAdded = { onInstructionAdded(it) },
        onInstructionUpdated = { key, text ->
            onInstructionUpdated(key, text)
        },
        onInstructionRemoved = { onInstructionRemoved(it) }
    )
}
