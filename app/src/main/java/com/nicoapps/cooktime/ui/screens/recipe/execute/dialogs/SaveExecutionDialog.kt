package com.nicoapps.cooktime.ui.screens.recipe.execute.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nicoapps.cooktime.R

@Composable
fun SaveExecutionDialog(
    onDismissRequest: () -> Unit,
    onConfirmed: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = stringResource(
                    id = R.string.execute_recipe_save_execution_dialog_title
                )
            )
        },
        text = {
            Text(
                text = stringResource(
                    id = R.string.execute_recipe_save_execution_dialog_description
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmed() }
            ) {
                Text(
                    text = stringResource(
                        id = R.string.execute_recipe_save_execution_dialog_confirm
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(
                    text = stringResource(
                        id = R.string.execute_recipe_save_execution_dialog_cancel
                    )
                )
            }
        }
    )
}