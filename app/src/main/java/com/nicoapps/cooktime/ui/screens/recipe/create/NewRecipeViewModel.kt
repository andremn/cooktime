package com.nicoapps.cooktime.ui.screens.recipe.create

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.ui.extensions.runWrite
import com.nicoapps.cooktime.ui.screens.recipe.draft.RecipeDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var savedDraft: RecipeDraft by savedStateHandle.saved(
        serializer = RecipeDraft.serializer(),
        key = KEY_DRAFT
    ) { RecipeDraft() }
    private var savedStep: NewRecipeScreenStep by savedStateHandle.saved(
        serializer = NewRecipeScreenStep.serializer(),
        key = KEY_STEP
    ) { NewRecipeScreenStep.NAME }

    private val _screenState = MutableStateFlow(
        NewRecipeScreenState(
            draft = savedDraft,
            currentStep = savedStep
        )
    )
    val screenState = _screenState.asStateFlow()

    fun nextStep() {
        val nextStep = _screenState.value.currentStep.nextStepOrNull() ?: return

        updateStep(nextStep)
    }

    fun previousStep() {
        val previousStep = _screenState.value.currentStep.previousStepOrNull() ?: return

        updateStep(previousStep)
    }

    fun onRecipeNameChanged(recipeName: String) =
        updateDraft(_screenState.value.draft.rename(recipeName))

    fun onRecipeIngredientAdded(name: String, quantity: Float, measurementUnit: String?) =
        updateDraft(
            _screenState.value.draft.addIngredient(
                name = name,
                quantity = quantity,
                measurementUnit = measurementUnit
            )
        )

    fun onRecipeIngredientUpdated(
        key: Long,
        name: String,
        quantity: Float,
        measurementUnit: String?
    ) =
        updateDraft(
            _screenState.value.draft.updateIngredient(
                key = key,
                name = name,
                quantity = quantity,
                measurementUnit = measurementUnit
            )
        )

    fun onRecipeIngredientRemoved(key: Long) =
        updateDraft(_screenState.value.draft.removeIngredient(key))

    fun onRecipeInstructionAdded(text: String) =
        updateDraft(_screenState.value.draft.addInstruction(text))

    fun onRecipeInstructionUpdated(key: Long, text: String) =
        updateDraft(_screenState.value.draft.updateInstruction(key, text))

    fun onRecipeInstructionRemoved(key: Long) =
        updateDraft(_screenState.value.draft.removeInstruction(key))

    fun saveRecipe() {
        val currentState = _screenState.value

        if (currentState.isSaving || !currentState.draft.isValid) {
            return
        }

        _screenState.update {
            it.copy(isSaving = true)
        }

        viewModelScope.launch {
            val saved = runWrite(
                onFailure = {
                    _screenState.update {
                        it.copy(
                            isSaving = false,
                            errorMessageResourceId = R.string.error_save_recipe
                        )
                    }
                }
            ) {
                recipeRepository.save(currentState.draft.toRecipe())
            }

            if (saved) {
                _screenState.update {
                    it.copy(recipeSaved = true)
                }
            }
        }
    }

    fun onErrorShown() =
        _screenState.update {
            it.copy(errorMessageResourceId = null)
        }

    private fun updateDraft(draft: RecipeDraft) {
        savedDraft = draft

        _screenState.update {
            it.copy(draft = draft)
        }
    }

    private fun updateStep(step: NewRecipeScreenStep) {
        savedStep = step

        _screenState.update {
            it.copy(currentStep = step)
        }
    }

    private companion object {
        const val KEY_DRAFT = "recipeDraft"
        const val KEY_STEP = "currentStep"
    }
}

data class NewRecipeScreenState(
    val draft: RecipeDraft = RecipeDraft(),
    val currentStep: NewRecipeScreenStep = NewRecipeScreenStep.NAME,
    val isSaving: Boolean = false,
    val recipeSaved: Boolean = false,
    @StringRes val errorMessageResourceId: Int? = null
) {
    val currentProgress: Float
        get() = currentStep.stepIndex.inc() / NewRecipeScreenStep.entries.size.toFloat()

    val backHandlerEnabled: Boolean
        get() = !currentStep.isFirstStep()

    val nextButtonEnabled: Boolean
        get() = !isSaving && when (currentStep) {
            NewRecipeScreenStep.NAME -> draft.isNameValid
            NewRecipeScreenStep.INGREDIENTS -> draft.areIngredientsValid
            NewRecipeScreenStep.INSTRUCTIONS -> draft.isValid
        }
}

@Serializable
enum class NewRecipeScreenStep(
    @StringRes val titleResourceId: Int,
    val stepIndex: Int
) {
    NAME(R.string.new_recipe_name_step_title, 0),
    INGREDIENTS(R.string.new_recipe_ingredients_step_title, 1),
    INSTRUCTIONS(R.string.new_recipe_instructions_step_title, 2);

    fun isFirstStep() =
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
