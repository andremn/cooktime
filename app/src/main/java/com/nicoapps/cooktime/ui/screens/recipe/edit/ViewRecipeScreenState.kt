package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.nicoapps.cooktime.ui.screens.recipe.draft.RecipeDraft

@Immutable
data class ViewRecipeScreenState(
    val selectedTab: ViewRecipeScreenTab = ViewRecipeScreenTab.INGREDIENTS,
    val isChangeNameDialogOpen: Boolean = false,
    val isDeleteConfirmationDialogOpen: Boolean = false,
    val isRecipeDeleted: Boolean = false,
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val recipeId: Long = 0L,
    val recipeName: String = "",
    val isRecipeStarred: Boolean = false,
    val recipeDraft: RecipeDraft = RecipeDraft(),
    val canSaveChanges: Boolean = false,
    @StringRes val errorMessageResourceId: Int? = null
)
