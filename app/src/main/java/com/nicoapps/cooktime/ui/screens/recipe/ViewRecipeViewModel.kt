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

    private val _screenState = MutableStateFlow(ViewRecipeScreenState())

    val screenState = _screenState.combine(recipeFlow) { screenState, recipe ->
        screenState.copy(
            recipeInstructions = recipe.instructions,
            recipeIngredients = recipe.ingredients
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ViewRecipeScreenState()
    )

    fun onSelectedTabChanged(tab: ViewRecipeScreenTab) {
        _screenState.update {
            it.copy(
                selectedTab = tab
            )
        }
    }

    fun onRecipeStarredChanged(isStarred: Boolean) {
        viewModelScope.launch {
            recipeRepository.updateIsStarred(recipeId, isStarred)
        }
    }

    fun onRecipeDeleteRequest() =
        _screenState.update {
            it.copy(
                isDeleteConfirmationDialogOpen = true
            )
        }

    fun dismissDeleteConfirmationDialog(confirmed: Boolean) {
        if (confirmed) {
            viewModelScope.launch {
                recipeRepository.deleteById(recipeId)

                _screenState.update {
                    it.copy(
                        isDeleteConfirmationDialogOpen = false,
                        isRecipeDeleted = true
                    )
                }
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    @Immutable
    data class ViewRecipeScreenState(
        val selectedTab: ViewRecipeScreenTab = ViewRecipeScreenTab.INGREDIENTS,
        val recipeName: String = "",
        val isRecipeDeleted: Boolean = false,
        val isRecipeStarred: Boolean = false,
        val isDeleteConfirmationDialogOpen: Boolean = false,
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