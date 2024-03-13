package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.ui.components.AppTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeInstructionsColumn(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    instructions: List<Instruction>,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onInstructionAdded: (Instruction) -> Unit,
    onInstructionUpdated: (Int, Instruction) -> Unit,
    onInstructionRemoved: (Instruction) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val isKeyboardOpen by keyboardAsState()
    val lazyColumnState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var currentInstructionText by remember { mutableStateOf("") }

    fun addInstruction() {
        if (currentInstructionText.isNotBlank()) {
            onInstructionAdded(
                Instruction(
                    text = currentInstructionText
                )
            )

            currentInstructionText = ""
            focusRequester.requestFocus()

            coroutineScope.launch {
                lazyColumnState.animateScrollToItem(instructions.size - 1)
                delay(BRING_INTO_VIEW_DELAY_MILLIS)
                bringIntoViewRequester.bringIntoView()
            }
        }
    }

    LaunchedEffect(key1 = isKeyboardOpen) {
        if (!isKeyboardOpen) {
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .animateContentSize(),
                state = lazyColumnState,
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                itemsIndexed(instructions) { index, instruction ->
                    key(index) {
                        RecipeInstructionGridItem(
                            index = index,
                            text = instruction.text,
                            isEditing = isEditing,
                            onTextChange = {
                                onInstructionUpdated(
                                    index,
                                    instruction.copy(
                                        text = it
                                    )
                                )
                            },
                            onRemoveClick = { onInstructionRemoved(instruction) }
                        )
                    }
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
                    autoCorrect = true
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                onValueChange = {
                    currentInstructionText = it
                }
            )
        }
    }
}

@Composable
private fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    return rememberUpdatedState(isImeVisible)
}

private const val BRING_INTO_VIEW_DELAY_MILLIS = 200L