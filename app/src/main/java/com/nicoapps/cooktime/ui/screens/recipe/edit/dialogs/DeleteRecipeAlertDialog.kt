package com.nicoapps.cooktime.ui.screens.recipe.edit.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nicoapps.cooktime.R

@Composable
fun DeleteRecipeAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmed: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = stringResource(
                    id = R.string.view_recipe_delete_confirmation_dialog_title
                )
            )
        },
        text = {
            Text(
                text = stringResource(
                    id = R.string.view_recipe_delete_confirmation_dialog_description
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmed() }
            ) {
                Text(
                    text = stringResource(
                        id = R.string.view_recipe_delete_confirmation_dialog_confirm
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(
                    text = stringResource(
                        id = R.string.view_recipe_delete_confirmation_dialog_cancel
                    )
                )
            }
        }
    )
}