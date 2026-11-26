package com.nicoapps.cooktime.ui.screens.recipe.edit.appbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.components.recipe.StarredRecipeIcon
import com.nicoapps.cooktime.ui.defaultTransitionSpec

@Composable
fun ViewRecipeAppBottomBarActions(
    modifier: Modifier = Modifier,
    isEditing: Boolean,
    isStarred: Boolean,
    isEditDoneEnabled: Boolean,
    isSaving: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStarClick: () -> Unit,
    onEditDoneClick: () -> Unit,
    onEditCancelClick: () -> Unit
) {
    AnimatedContent(
        targetState = isEditing,
        label = "viewRecipeAppBottomBarActionsAnimation",
        transitionSpec = { defaultTransitionSpec() }
    ) { editing ->
        Row(
            modifier = modifier
        ) {
            if (editing) {
                IconButton(
                    enabled = isEditDoneEnabled && !isSaving,
                    onClick = { onEditDoneClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.cd_save_changes)
                    )
                }

                IconButton(
                    enabled = !isSaving,
                    onClick = { onEditCancelClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cd_discard_changes)
                    )
                }
            } else {
                IconButton(onClick = { onEditClick() }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.cd_edit_recipe),
                    )
                }

                IconButton(onClick = { onDeleteClick() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cd_delete_recipe),
                    )
                }

                IconButton(
                    onClick = { onStarClick() }) {
                    StarredRecipeIcon(
                        isStarred = isStarred
                    )
                }
            }
        }
    }
}