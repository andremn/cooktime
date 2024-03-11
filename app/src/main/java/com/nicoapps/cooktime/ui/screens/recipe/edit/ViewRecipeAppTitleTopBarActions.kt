package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ViewRecipeAppTitleTopBarActions(
    modifier: Modifier = Modifier,
    onDoneClick: () -> Unit,
    onCancelClick: () -> Unit,
    onChangeNameClick: () -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Row(modifier = modifier) {
        IconButton(
            onClick = { onCancelClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Localized description"
            )
        }

        IconButton(
            onClick = { onDoneClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Localized description"
            )
        }

        IconButton(onClick = { isDropdownExpanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Localized description"
            )
        }

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "Change name")
                },
                onClick = {
                    isDropdownExpanded = false
                    onChangeNameClick()
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = "Localized description"
                    )
                }
            )
        }
    }
}