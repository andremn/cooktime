package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.components.AppTextField
import com.nicoapps.cooktime.ui.screens.recipe.draft.DraftInstruction
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeInstructionsColumn(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    instructions: List<DraftInstruction>,
    onInstructionAdded: (text: String) -> Unit,
    onInstructionUpdated: (key: Long, text: String) -> Unit,
    onInstructionRemoved: (key: Long) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val isKeyboardOpen = WindowInsets.isImeVisible
    val lazyColumnState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var currentInstructionText by rememberSaveable { mutableStateOf("") }

    // Updated after every list change so the effect below can tell an appended instruction from
    // any other change. A fresh composition (opening the step, rotating) starts with the current
    // list and therefore does not scroll.
    var previousInstructionKeys by remember { mutableStateOf(instructions.map { it.key }) }

    fun addInstruction() {
        if (currentInstructionText.isNotBlank()) {
            onInstructionAdded(currentInstructionText)

            currentInstructionText = ""
            focusRequester.requestFocus()
        }
    }

    LaunchedEffect(key1 = isKeyboardOpen) {
        if (!isKeyboardOpen) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(instructions) {
        val currentInstructionKeys = instructions.map { it.key }
        val instructionAppended = wasInstructionAppended(
            previousKeys = previousInstructionKeys,
            currentKeys = currentInstructionKeys
        )

        previousInstructionKeys = currentInstructionKeys

        if (isEditing && instructionAppended) {
            lazyColumnState.animateScrollToItem(instructions.lastIndex)
            delay(BRING_INTO_VIEW_DELAY_MILLIS.milliseconds)
            bringIntoViewRequester.bringIntoView()
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (instructions.isEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.new_recipe_add_instruction_empty),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(
                // Takes the height left after the new instruction field, so a long list scrolls
                // inside the screen and shrinks with the keyboard instead of pushing the field
                // below it.
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .animateContentSize(),
                state = lazyColumnState,
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                itemsIndexed(
                    items = instructions,
                    key = { _, instruction -> instruction.key }
                ) { index, instruction ->
                    RecipeInstructionGridItem(
                        index = index,
                        text = instruction.text,
                        isEditing = isEditing,
                        onTextChange = { onInstructionUpdated(instruction.key, it) },
                        onRemoveClick = { onInstructionRemoved(instruction.key) }
                    )
                }
            }
        }

        if (isEditing) {
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .padding(bottom = 10.dp),
                value = currentInstructionText,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.new_recipe_add_instruction_placeholder)
                    )
                },
                keyboardActions = KeyboardActions(
                    onNext = { addInstruction() }
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true
                ),
                leadingIcon = {
                    IconButton(
                        onClick = { addInstruction() },
                        enabled = currentInstructionText.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.cd_add_instruction)
                        )
                    }
                },
                onValueChange = {
                    currentInstructionText = it
                }
            )
        }
    }
}

private const val BRING_INTO_VIEW_DELAY_MILLIS = 200L

/** Whether [currentKeys] is [previousKeys] with one or more instructions appended at the end. */
internal fun wasInstructionAppended(
    previousKeys: List<Long>,
    currentKeys: List<Long>
) =
    currentKeys.size > previousKeys.size &&
            currentKeys.take(previousKeys.size) == previousKeys
