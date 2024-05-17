package com.nicoapps.cooktime.ui.components.recipe

enum class RecipeIngredientsGridMode {
    READ_ONLY,
    EDIT;

    fun isEditing() = this == EDIT

    companion object {
        fun fromIsEditing(isEditing: Boolean) =
            if (isEditing) EDIT
            else READ_ONLY
    }
}