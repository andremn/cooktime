package com.nicoapps.cooktime.ui.screens.recipe.create.steps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.ui.components.recipe.RecipeInstructionsColumn
import kotlinx.coroutines.CoroutineScope

@Composable
fun NewRecipeInstructionsStep(
    modifier: Modifier = Modifier,
    instructions: List<Instruction>,
    onCurrentTitleChanged: (String) -> Unit,
    onInstructionAdded: (Instruction) -> Unit,
    onInstructionUpdated: (Int, Instruction) -> Unit,
    onInstructionRemoved: (Instruction) -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onCurrentTitleChanged(
            context.resources.getString(R.string.new_recipe_instructions_step_title)
        )
    }

    RecipeInstructionsColumn(
        modifier = modifier,
        isEditing = true,
        coroutineScope = coroutineScope,
        instructions = instructions,
        onInstructionAdded = { onInstructionAdded(it) },
        onInstructionUpdated = { index, instruction ->
            onInstructionUpdated(
                index,
                instruction
            )
        },
        onInstructionRemoved = { onInstructionRemoved(it) }
    )
}