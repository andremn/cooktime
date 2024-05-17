package com.nicoapps.cooktime.ui.screens.recipe.edit.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.dialogSurfaceColor
import com.nicoapps.cooktime.ui.dialogTextFieldColors

@Composable
fun EditRecipeNameDialog(
    modifier: Modifier = Modifier,
    recipeName: String,
    onDismissRequest: () -> Unit,
    onConfirmed: (String) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = dialogSurfaceColor()
            )
        ) {
            val focusRequester = remember { FocusRequester() }
            var textFieldValue by remember {
                mutableStateOf(
                    TextFieldValue(
                        text = recipeName,
                        selection = TextRange(
                            start = recipeName.length,
                            end = recipeName.length
                        )
                    )
                )
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Text(
                modifier = modifier.padding(
                    dimensionResource(id = R.dimen.dialog_title_spacing)
                ),
                text = stringResource(id = R.string.change_recipe_name_dialog_title),
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                modifier = modifier
                    .padding(dimensionResource(id = R.dimen.text_input_padding))
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = stringResource(
                            R.string.change_recipe_name_placeholder
                        )
                    )
                },
                colors = dialogTextFieldColors(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                singleLine = true,
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                }
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.dialog_buttons_spacing)
                    ),
                    onClick = { onDismissRequest() }
                ) {
                    Text(
                        text = stringResource(id = R.string.new_recipe_add_ingredient_cancel)
                    )
                }

                TextButton(
                    modifier = modifier.padding(
                        dimensionResource(id = R.dimen.dialog_buttons_spacing)
                    ),
                    enabled = recipeName.isNotBlank(),
                    onClick = { onConfirmed(textFieldValue.text) }
                ) {
                    Text(
                        text = stringResource(id = R.string.new_recipe_add_ingredient_done)
                    )
                }
            }
        }
    }
}