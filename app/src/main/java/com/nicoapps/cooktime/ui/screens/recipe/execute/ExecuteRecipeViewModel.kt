package com.nicoapps.cooktime.ui.screens.recipe.execute

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.data.RecipeExecutionRepository
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.Recipe
import com.nicoapps.cooktime.model.RecipeExecution
import com.nicoapps.cooktime.ui.ExecuteRecipe
import com.nicoapps.cooktime.ui.extensions.runWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExecuteRecipeViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository,
    @LocalRepository private val recipeExecutionRepository: RecipeExecutionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val destination: ExecuteRecipe = savedStateHandle.toRoute()
    private val recipeId: Long = destination.recipeId
    private val recipeTitle: String = destination.recipeTitle

    private val checkedIngredientIds = savedStateHandle.getMutableStateFlow(
        KEY_CHECKED_INGREDIENTS,
        LongArray(0)
    )
    private val checkedInstructionIds = savedStateHandle.getMutableStateFlow(
        KEY_CHECKED_INSTRUCTIONS,
        LongArray(0)
    )
    private val uiState = MutableStateFlow(ExecuteRecipeUiState())

    private val recipe = recipeRepository.findById(recipeId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = null
        )

    val screenState = combine(
        recipe,
        checkedIngredientIds,
        checkedInstructionIds,
        uiState
    ) { recipe, checkedIngredientIds, checkedInstructionIds, uiState ->
        createScreenState(
            recipe = recipe,
            checkedIngredientIds = checkedIngredientIds.toSet(),
            checkedInstructionIds = checkedInstructionIds.toSet(),
            uiState = uiState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = createScreenState(
            recipe = null,
            checkedIngredientIds = emptySet(),
            checkedInstructionIds = emptySet(),
            uiState = uiState.value
        )
    )

    fun onIngredientCheckChanged(ingredientId: Long, isChecked: Boolean) =
        checkedIngredientIds.update { checkedIds ->
            checkedIds.withChecked(ingredientId, isChecked)
        }

    fun onInstructionCheckChanged(instructionId: Long, isChecked: Boolean) =
        checkedInstructionIds.update { checkedIds ->
            checkedIds.withChecked(instructionId, isChecked)
        }

    fun onReset() {
        checkedIngredientIds.value = LongArray(0)
        checkedInstructionIds.value = LongArray(0)
    }

    fun onBackPressed() =
        updateUiState {
            it.copy(isSaveExecutionDialogOpen = true)
        }

    fun dismissSaveExecutionDialog() =
        updateUiState {
            it.copy(isSaveExecutionDialogOpen = false)
        }

    fun onSaveExecutionDialogConfirmed() {
        if (uiState.value.isSaving) {
            return
        }

        updateUiState {
            it.copy(isSaving = true)
        }

        viewModelScope.launch {
            val saved = runWrite(
                onFailure = {
                    updateUiState {
                        it.copy(
                            isSaveExecutionDialogOpen = false,
                            isSaving = false,
                            errorMessageResourceId = R.string.error_save_recipe_execution
                        )
                    }
                }
            ) {
                recipeExecutionRepository.save(
                    RecipeExecution(
                        recipeId = recipeId
                    )
                )
            }

            if (saved) {
                updateUiState {
                    it.copy(
                        isSaveExecutionDialogOpen = false,
                        isSaving = false,
                        isExecutionSaved = true
                    )
                }
            }
        }
    }

    fun onErrorShown() =
        updateUiState {
            it.copy(errorMessageResourceId = null)
        }

    private fun updateUiState(transform: (ExecuteRecipeUiState) -> ExecuteRecipeUiState) {
        uiState.update(transform)
    }

    private fun createScreenState(
        recipe: Recipe?,
        checkedIngredientIds: Set<Long>,
        checkedInstructionIds: Set<Long>,
        uiState: ExecuteRecipeUiState
    ) =
        ExecuteRecipeScreenState(
            recipeId = recipeId,
            recipeName = recipe?.name ?: recipeTitle,
            ingredients = recipe?.ingredients.orEmpty().map { ingredient ->
                IngredientState(
                    ingredient = ingredient,
                    isChecked = ingredient.id in checkedIngredientIds
                )
            },
            instructions = recipe?.instructions.orEmpty().map { instruction ->
                InstructionState(
                    instruction = instruction,
                    isChecked = instruction.id in checkedInstructionIds
                )
            },
            isSaveExecutionDialogOpen = uiState.isSaveExecutionDialogOpen,
            isSaving = uiState.isSaving,
            isExecutionSaved = uiState.isExecutionSaved,
            errorMessageResourceId = uiState.errorMessageResourceId
        )

    private data class ExecuteRecipeUiState(
        val isSaveExecutionDialogOpen: Boolean = false,
        val isSaving: Boolean = false,
        val isExecutionSaved: Boolean = false,
        @StringRes val errorMessageResourceId: Int? = null
    )

    private companion object {
        private const val KEY_CHECKED_INGREDIENTS = "checkedIngredientIds"
        private const val KEY_CHECKED_INSTRUCTIONS = "checkedInstructionIds"
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

private fun LongArray.withChecked(id: Long, isChecked: Boolean) =
    if (isChecked) {
        if (contains(id)) this else this + id
    } else {
        filterNot { it == id }.toLongArray()
    }
