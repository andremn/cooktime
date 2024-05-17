package com.nicoapps.cooktime.ui.screens.recipe.edit

enum class ViewRecipeScreenTab(val index: Int) {
    INGREDIENTS(0),
    INSTRUCTIONS(1);

    companion object {
        fun fromIndex(index: Int) =
            entries[index]
    }
}