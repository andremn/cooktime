package com.nicoapps.cooktime.ui.screens.recipe

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewRecipeViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val recipeId: Int = checkNotNull(savedStateHandle["recipeId"])
    private val recipeFlow = recipeRepository.findById(recipeId)
        .filterNotNull()

    private val selectedTabFlow = MutableStateFlow(ViewRecipeScreenTab.INGREDIENTS)

    val screenState = combine(selectedTabFlow, recipeFlow) { selectedTab, recipe ->
        ViewRecipeScreenState(
            selectedTab = selectedTab,
            recipeName = recipe.name,
            isRecipeStarred = recipe.isStarred,
            recipeInstructions = recipe.instructions,
            recipeIngredients = recipe.ingredients
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ViewRecipeScreenState()
    )

    fun onSelectedTabChanged(tab: ViewRecipeScreenTab) {
        selectedTabFlow.update { tab }
    }

    fun onRecipeStarredChanged(isStarred: Boolean) {
        viewModelScope.launch {
            recipeRepository.updateIsStarred(recipeId, isStarred)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    @Immutable
    data class ViewRecipeScreenState(
        val selectedTab: ViewRecipeScreenTab = ViewRecipeScreenTab.INGREDIENTS,
        val recipeName: String = "",
        val isRecipeStarred: Boolean = false,
        val recipeIngredients: List<Ingredient> = emptyList(),
        val recipeInstructions: List<Instruction> = emptyList()
    )

    enum class ViewRecipeScreenTab(val index: Int) {
        INGREDIENTS(0),
        INSTRUCTIONS(1);

        companion object {
            fun fromIndex(index: Int) =
                entries[index]
        }
    }
}