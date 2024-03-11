package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.dialogSurfaceColor
import com.nicoapps.cooktime.ui.dialogTextFieldColors

@Composable
fun NewIngredientDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    name: String,
    quantity: String,
    measurementUnit: String,
    onNameChanged: (String) -> Unit,
    onQuantityChanged: (String) -> Unit,
    onMeasurementUnitChanged: (String) -> Unit,
    onCloseRequest: (Boolean) -> Unit
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = { onCloseRequest(false) },
            properties = DialogProperties(
                usePlatformDefaultWidth = true,
                decorFitsSystemWindows = false
            )
        ) {
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = dialogSurfaceColor()
                )
            ) {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(
                            dimensionResource(id = R.dimen.dialog_title_spacing)
                        ),
                        text = stringResource(id = R.string.new_recipe_new_ingredient_dialog_title),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.text_input_padding)),
                        placeholder = {
                            Text(
                                text = stringResource(
                                    R.string.new_recipe_add_ingredient_name_placeholder
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
                        value = name,
                        onValueChange = {
                            onNameChanged(it)
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.text_input_padding)),
                        value = quantity,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = stringResource(
                                    R.string.new_recipe_add_ingredient_quantity_placeholder
                                )
                            )
                        },
                        colors = dialogTextFieldColors(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            onQuantityChanged(it)
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.text_input_padding)),
                        value = measurementUnit,
                        singleLine = true,
                        placeholder = {
                            Text(
                                text = stringResource(
                                    R.string.new_recipe_add_ingredient_unit_placeholder
                                )
                            )
                        },
                        colors = dialogTextFieldColors(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.None
                        ),
                        onValueChange = { onMeasurementUnitChanged(it) }
                    )

                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            modifier = Modifier.padding(
                                dimensionResource(id = R.dimen.dialog_buttons_spacing)
                            ),
                            onClick = { onCloseRequest(false) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.new_recipe_add_ingredient_cancel)
                            )
                        }

                        TextButton(
                            modifier = Modifier.padding(
                                dimensionResource(id = R.dimen.dialog_buttons_spacing)
                            ),
                            enabled = name.isNotBlank() && quantity.isNotBlank(),
                            onClick = { onCloseRequest(true) }
                        ) {
                            Text(
                                text = stringResource(id = R.string.new_recipe_add_ingredient_done)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NewIngredientDialogPreview() {
    NewIngredientDialog(
        name = "",
        quantity = "",
        measurementUnit = "",
        isOpen = true,
        onNameChanged = {},
        onQuantityChanged = {},
        onMeasurementUnitChanged = {},
        onCloseRequest = {}
    )
}