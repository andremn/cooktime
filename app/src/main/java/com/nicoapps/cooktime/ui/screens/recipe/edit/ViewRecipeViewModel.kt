package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.serialization.saved
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.Recipe
import com.nicoapps.cooktime.ui.ViewRecipe
import com.nicoapps.cooktime.ui.extensions.runWrite
import com.nicoapps.cooktime.ui.screens.recipe.draft.RecipeDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.builtins.nullable
import javax.inject.Inject

@HiltViewModel
class ViewRecipeViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val destination: ViewRecipe = savedStateHandle.toRoute()
    private val recipeId: Long = destination.recipeId
    private val recipeTitle: String = destination.recipeTitle

    private var savedDraft: RecipeDraft? by savedStateHandle.saved(
        serializer = RecipeDraft.serializer().nullable,
        key = KEY_DRAFT
    ) { null }
    private val draft = MutableStateFlow(savedDraft)
    private val isEditing = savedStateHandle.getMutableStateFlow(KEY_IS_EDITING, false)
    private val uiState = MutableStateFlow(ViewRecipeUiState())

    private val recipe = recipeRepository.findById(recipeId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = null
        )

    val screenState = combine(
        recipe,
        draft,
        isEditing,
        uiState
    ) { persistedRecipe, draft, isEditing, uiState ->
        createScreenState(persistedRecipe, draft, isEditing, uiState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = createScreenState(
            persistedRecipe = null,
            draft = draft.value,
            isEditing = isEditing.value,
            uiState = uiState.value
        )
    )

    fun onSelectedTabChanged(tab: ViewRecipeScreenTab) =
        updateUiState {
            it.copy(selectedTab = tab)
        }

    fun onRecipeStarredChanged(isStarred: Boolean) {
        viewModelScope.launch {
            runWrite(onFailure = { reportError(R.string.error_update_recipe_star) }) {
                recipeRepository.updateIsStarred(recipeId, isStarred)
            }
        }
    }

    fun onRecipeDeleteRequest() =
        updateUiState {
            it.copy(isDeleteConfirmationDialogOpen = true)
        }

    fun dismissDeleteConfirmationDialog() =
        updateUiState {
            it.copy(isDeleteConfirmationDialogOpen = false)
        }

    fun deleteRecipe() {
        updateUiState {
            it.copy(isDeleteConfirmationDialogOpen = false)
        }

        viewModelScope.launch {
            val deleted = runWrite(onFailure = { reportError(R.string.error_delete_recipe) }) {
                recipeRepository.deleteById(recipeId)
            }

            if (deleted) {
                updateUiState {
                    it.copy(isRecipeDeleted = true)
                }
            }
        }
    }

    fun onEditClick() {
        val persistedRecipe = recipe.value ?: return

        updateDraft(RecipeDraft.from(persistedRecipe))
        isEditing.value = true
    }

    fun onFinishEditing(saveChanges: Boolean) {
        if (uiState.value.isSaving) {
            return
        }

        if (saveChanges) {
            saveChanges()
        } else {
            discardChanges()
        }
    }

    fun onEditNameClick() =
        updateUiState {
            it.copy(isChangeNameDialogOpen = true)
        }

    fun dismissChangeNameDialog() =
        updateUiState {
            it.copy(isChangeNameDialogOpen = false)
        }

    fun onRecipeNameChanged(recipeName: String) {
        val currentDraft = draft.value ?: return

        updateDraft(currentDraft.rename(recipeName))
    }

    fun onRecipeIngredientAdded(name: String, quantity: Float, measurementUnit: String?) {
        val currentDraft = draft.value ?: return

        updateDraft(
            currentDraft.addIngredient(
                name = name,
                quantity = quantity,
                measurementUnit = measurementUnit
            )
        )
    }

    fun onRecipeIngredientUpdated(
        key: Long,
        name: String,
        quantity: Float,
        measurementUnit: String?
    ) {
        val currentDraft = draft.value ?: return

        updateDraft(
            currentDraft.updateIngredient(
                key = key,
                name = name,
                quantity = quantity,
                measurementUnit = measurementUnit
            )
        )
    }

    fun onRecipeIngredientRemoved(key: Long) {
        val currentDraft = draft.value ?: return

        updateDraft(currentDraft.removeIngredient(key))
    }

    fun onRecipeInstructionAdded(text: String) {
        val currentDraft = draft.value ?: return

        updateDraft(currentDraft.addInstruction(text))
    }

    fun onRecipeInstructionUpdated(key: Long, text: String) {
        val currentDraft = draft.value ?: return

        updateDraft(currentDraft.updateInstruction(key, text))
    }

    fun onRecipeInstructionRemoved(key: Long) {
        val currentDraft = draft.value ?: return

        updateDraft(currentDraft.removeInstruction(key))
    }

    fun onErrorShown() =
        updateUiState {
            it.copy(errorMessageResourceId = null)
        }

    private fun saveChanges() {
        val currentDraft = draft.value ?: return

        if (!currentDraft.isValid) {
            return
        }

        updateUiState {
            it.copy(isSaving = true)
        }

        viewModelScope.launch {
            val saved = runWrite(onFailure = { reportError(R.string.error_save_recipe) }) {
                recipeRepository.save(
                    currentDraft.toRecipe(
                        id = recipeId,
                        isStarred = recipe.value?.isStarred ?: false
                    )
                )

                // Stay in the draft until the database emits the recipe it just saved, so the
                // screen never falls back to the previous persisted content for a frame.
                withTimeoutOrNull(REFRESH_TIMEOUT_MILLIS) {
                    recipe.first { persistedRecipe ->
                        persistedRecipe != null &&
                                RecipeDraft.from(persistedRecipe).hasSameContentAs(currentDraft)
                    }
                }
            }

            if (saved) {
                isEditing.value = false
                updateDraft(null)
            }

            updateUiState {
                it.copy(isSaving = false)
            }
        }
    }

    private fun reportError(@StringRes errorMessageResourceId: Int) =
        updateUiState {
            it.copy(errorMessageResourceId = errorMessageResourceId)
        }

    private fun discardChanges() {
        isEditing.value = false
        updateDraft(null)
    }

    private fun updateDraft(draft: RecipeDraft?) {
        savedDraft = draft
        this.draft.value = draft
    }

    private fun updateUiState(transform: (ViewRecipeUiState) -> ViewRecipeUiState) {
        uiState.update(transform)
    }

    private fun createScreenState(
        persistedRecipe: Recipe?,
        draft: RecipeDraft?,
        isEditing: Boolean,
        uiState: ViewRecipeUiState
    ): ViewRecipeScreenState {
        val displayedDraft = if (isEditing) {
            draft
        } else {
            persistedRecipe?.let { RecipeDraft.from(it) }
        }

        return ViewRecipeScreenState(
            selectedTab = uiState.selectedTab,
            isChangeNameDialogOpen = uiState.isChangeNameDialogOpen,
            isDeleteConfirmationDialogOpen = uiState.isDeleteConfirmationDialogOpen,
            isRecipeDeleted = uiState.isRecipeDeleted,
            isEditing = isEditing,
            isSaving = uiState.isSaving,
            recipeId = recipeId,
            recipeName = displayedDraft?.name ?: recipeTitle,
            isRecipeStarred = persistedRecipe?.isStarred ?: false,
            recipeDraft = displayedDraft ?: RecipeDraft(name = recipeTitle),
            canSaveChanges = isEditing && draft?.isValid == true,
            errorMessageResourceId = uiState.errorMessageResourceId
        )
    }

    private data class ViewRecipeUiState(
        val selectedTab: ViewRecipeScreenTab = ViewRecipeScreenTab.INGREDIENTS,
        val isChangeNameDialogOpen: Boolean = false,
        val isDeleteConfirmationDialogOpen: Boolean = false,
        val isRecipeDeleted: Boolean = false,
        val isSaving: Boolean = false,
        @StringRes val errorMessageResourceId: Int? = null
    )

    private companion object {
        private const val KEY_DRAFT = "recipeDraft"
        private const val KEY_IS_EDITING = "isEditing"
        private const val TIMEOUT_MILLIS = 5_000L
        private const val REFRESH_TIMEOUT_MILLIS = 1_000L
    }
}
