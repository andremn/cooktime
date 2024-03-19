package com.nicoapps.cooktime.ui.screens.recipe.execute

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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExecuteRecipeViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val recipeId: Long = checkNotNull(savedStateHandle["recipeId"])

    private val _screenState = MutableStateFlow(ExecuteRecipeScreenState())

    init {
        recipeRepository.findById(recipeId)
            .filterNotNull()
            .onEach { recipe ->
                _screenState.update {
                    it.copy(
                        recipeName = recipe.name,
                        ingredients = recipe.ingredients.map { ingredient ->
                            IngredientState(
                                ingredient = ingredient,
                                isChecked = false
                            )
                        },
                        instructions = recipe.instructions.map { instruction ->
                            InstructionState(
                                instruction = instruction,
                                isChecked = false
                            )
                        }
                    )
                }
            }.launchIn(viewModelScope)
    }

    val screenState = _screenState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ExecuteRecipeScreenState()
        )

    fun onIngredientCheckChanged(ingredientIndex: Int, isChecked: Boolean) {
        _screenState.update {
            val mutableIngredients = it.ingredients.toMutableList()
            val ingredientToChange = mutableIngredients[ingredientIndex]

            mutableIngredients[ingredientIndex] = ingredientToChange.copy(
                isChecked = isChecked
            )

            it.copy(
                ingredients = mutableIngredients
            )
        }
    }

    fun onInstructionCheckChanged(instructionIndex: Int, isChecked: Boolean) {
        _screenState.update {
            val mutableInstructions = it.instructions.toMutableList()
            val instructionToChange = mutableInstructions[instructionIndex]

            mutableInstructions[instructionIndex] = instructionToChange.copy(
                isChecked = isChecked
            )

            it.copy(
                instructions = mutableInstructions
            )
        }
    }

    data class ExecuteRecipeScreenState(
        val recipeName: String = "",
        val ingredients: List<IngredientState> = emptyList(),
        val instructions: List<InstructionState> = emptyList()
    )

    data class IngredientState(
        val ingredient: Ingredient,
        val isChecked: Boolean
    )

    data class InstructionState(
        val instruction: Instruction,
        val isChecked: Boolean
    )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}