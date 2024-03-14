package com.nicoapps.cooktime.ui.screens.recipe.edit.actions

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import com.nicoapps.cooktime.ui.defaultTransitionSpec

@Composable
fun ViewRecipeAppBottomBarActions(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    isStarred: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStarClick: () -> Unit,
    onEditDoneClick: () -> Unit,
    onEditCancelClick: () -> Unit,
    onChangeNameClick: () -> Unit
) {
    AnimatedContent(
        targetState = isEditing,
        label = "viewRecipeAppBottomBarActionsAnimation",
        transitionSpec = { defaultTransitionSpec() }
    ) { editing ->
        Row(
            modifier = modifier
        ) {
            var isDropdownExpanded by remember { mutableStateOf(false) }
            if (editing) {
                IconButton(onClick = { isDropdownExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Localized description"
                    )
                }

                IconButton(
                    onClick = { onEditDoneClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Localized description"
                    )
                }

                IconButton(
                    onClick = { onEditCancelClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
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
            } else {
                IconButton(onClick = { onEditClick() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Localized description",
                    )
                }

                IconButton(onClick = { onDeleteClick() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Localized description",
                    )
                }

                IconButton(
                    onClick = { onStarClick() }) {
                    Icon(
                        imageVector =
                        if (isStarred)
                            Icons.Default.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "Localized description",
                    )
                }
            }
        }
    }
}