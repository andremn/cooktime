package com.nicoapps.cooktime.ui.screens.recipe

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _ingredients = mutableStateListOf<Ingredient>()
    private val _instructions = mutableStateListOf<Instruction>()
    private val _screenState = MutableStateFlow(
        NewRecipeScreenState(
            currentProgress = calculateProgress(NewRecipeScreenStep.NAME),
            recipeIngredients = _ingredients,
            recipeInstructions = _instructions
        )
    )

    val screenState = _screenState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        _screenState.value
    )

    fun nextStep() {
        _screenState.update {
            val nextStep = it.currentStep.nextStepOrNull() ?: it.currentStep

            it.copy(
                currentStep = nextStep,
                currentProgress = calculateProgress(nextStep),
                backHandlerEnabled = nextStep.isBackHandlerEnabled(),
                nextButtonEnabled = isNextButtonEnabled(it, nextStep)
            )
        }
    }

    fun previousStep() {
        _screenState.update {
            val previousStep = it.currentStep.previousStepOrNull() ?: it.currentStep

            it.copy(
                currentStep = previousStep,
                currentProgress = calculateProgress(previousStep),
                backHandlerEnabled = previousStep.isBackHandlerEnabled(),
                nextButtonEnabled = isNextButtonEnabled(it, previousStep)
            )
        }
    }

    fun onCurrentTitleChanged(title: String) {
        _screenState.update {
            it.copy(
                currentTitle = title
            )
        }
    }

    fun onRecipeNameChanged(recipeName: String) {
        _screenState.update {
            it.copy(
                recipeName = recipeName,
                nextButtonEnabled = recipeName.isNotBlank()
            )
        }
    }

    fun onRecipeIngredientAdded(ingredient: Ingredient) {
        _ingredients.add(ingredient)

        _screenState.update {
            it.copy(
                nextButtonEnabled = _ingredients.isNotEmpty()
            )
        }
    }

    fun onRecipeIngredientRemoved(ingredient: Ingredient) {
        _ingredients.remove(ingredient)

        _screenState.update {
            it.copy(
                nextButtonEnabled = _ingredients.isNotEmpty()
            )
        }
    }

    fun onRecipeIngredientUpdated(index: Int, ingredient: Ingredient) {
        if (index in 0..<_ingredients.size) {
            _ingredients[index] = ingredient
        }
    }

    fun onRecipeInstructionAdded(instruction: Instruction) {
        _instructions.add(instruction)

        _screenState.update {
            it.copy(
                nextButtonEnabled = _instructions.isNotEmpty()
            )
        }
    }

    fun onRecipeInstructionRemoved(instruction: Instruction) {
        _instructions.remove(instruction)

        _screenState.update {
            it.copy(
                nextButtonEnabled = _instructions.isNotEmpty()
            )
        }
    }

    fun onRecipeInstructionUpdated(index: Int, instruction: Instruction) {
        if (index >= 0 && index < _instructions.size) {
            _instructions[index] = instruction
        }
    }

    fun saveRecipe() {
        viewModelScope.launch {
            recipeRepository.save(
                Recipe(
                    name = screenState.value.recipeName,
                    ingredients = screenState.value.recipeIngredients,
                    instructions = screenState.value.recipeInstructions,
                    isStarred = false
                )
            )

            _screenState.update {
                it.copy(
                    recipeSaved = true
                )
            }
        }
    }

    private fun isNextButtonEnabled(
        currentState: NewRecipeScreenState,
        targetStep: NewRecipeScreenStep
    ) =
        when (targetStep) {
            NewRecipeScreenStep.NAME -> currentState.recipeName.isNotBlank()
            NewRecipeScreenStep.INGREDIENTS -> currentState.recipeIngredients.isNotEmpty()
            NewRecipeScreenStep.INSTRUCTIONS -> currentState.recipeInstructions.isNotEmpty()
        }

    private fun NewRecipeScreenStep.isBackHandlerEnabled() =
        !isFistStep()

    private fun calculateProgress(step: NewRecipeScreenStep) =
        step.stepIndex.inc() / NewRecipeScreenStep.entries.size.toFloat()
}

data class NewRecipeScreenState(
    val currentStep: NewRecipeScreenStep = NewRecipeScreenStep.NAME,
    val currentProgress: Float = 0f,
    val currentTitle: String = "",
    val recipeName: String = "",
    val recipeIngredients: List<Ingredient> = emptyList(),
    val recipeInstructions: List<Instruction> = emptyList(),
    val nextButtonEnabled: Boolean = false,
    val backHandlerEnabled: Boolean = false,
    val recipeSaved: Boolean = false
)

enum class NewRecipeScreenStep(val stepIndex: Int) {
    NAME(0),
    INGREDIENTS(1),
    INSTRUCTIONS(2);

    fun isFistStep() =
        stepIndex == NAME.stepIndex

    fun isLastStep() =
        stepIndex == INSTRUCTIONS.stepIndex

    fun nextStepOrNull(): NewRecipeScreenStep? {
        if (stepIndex == INSTRUCTIONS.stepIndex) {
            return null
        }

        return fromStepIndex(stepIndex.inc())
    }


    fun previousStepOrNull(): NewRecipeScreenStep? {
        if (stepIndex == NAME.stepIndex) {
            return null
        }

        return fromStepIndex(stepIndex.dec())
    }

    companion object {
        fun fromStepIndex(stepIndex: Int) =
            NewRecipeScreenStep.entries[stepIndex]
    }
}