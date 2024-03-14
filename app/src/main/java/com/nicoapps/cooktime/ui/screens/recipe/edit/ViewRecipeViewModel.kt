package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ViewRecipeViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val recipeId: Long = checkNotNull(savedStateHandle["recipeId"])
    private val recipeState = MutableStateFlow(RecipeState())
    private val _screenState = MutableStateFlow(ScreenState())

    private lateinit var editingRecipe: Recipe

    init {
        recipeRepository.findById(recipeId)
            .filterNotNull()
            .onEach { recipe ->
                recipeState.update {
                    it.copy(
                        name = recipe.name,
                        isStarred = recipe.isStarred,
                        ingredients = recipe.ingredients,
                        instructions = recipe.instructions
                    )
                }
            }.launchIn(viewModelScope)
    }

    val screenState = _screenState.combine(recipeState) { screenState, recipeState ->
        createViewRecipeScreenState(screenState, recipeState)
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

    fun dismissDeleteConfirmationDialog() {
        _screenState.update {
            it.copy(
                isDeleteConfirmationDialogOpen = false
            )
        }
    }

    fun deleteRecipe() {
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

    fun onEditClick() {
        _screenState.update {
            it.copy(
                isEditing = true
            )
        }

        editingRecipe = recipeState.value.toRecipe()
    }

    fun onFinishEditing(saveChanges: Boolean) {
        _screenState.update {
            it.copy(
                isEditing = false
            )
        }

        if (saveChanges) {
            viewModelScope.launch {
                recipeRepository.save(
                    recipe = recipeState.value.toRecipe(recipeId)
                )
            }
        } else {
            rollbackChanges()
        }
    }

    fun onEditNameClick() {
        _screenState.update {
            it.copy(
                isChangeNameDialogOpen = true
            )
        }
    }

    fun dismissChangeNameDialog() {
        _screenState.update {
            it.copy(
                isChangeNameDialogOpen = false
            )
        }
    }

    fun onRecipeNameChanged(recipeName: String) {
        recipeState.update {
            it.copy(
                name = recipeName
            )
        }
    }

    fun onRecipeIngredientAdded(ingredient: Ingredient) {
        recipeState.update {
            it.copy(
                ingredients = it.ingredients + ingredient
            )
        }
    }

    fun onRecipeIngredientRemoved(ingredient: Ingredient) {
        recipeState.update {
            it.copy(
                ingredients = it.ingredients - ingredient
            )
        }
    }

    fun onRecipeIngredientUpdated(index: Int, ingredient: Ingredient) {
        recipeState.update {
            if (index in 0..<it.ingredients.size) {
                val ingredients = it.ingredients.toMutableList()

                ingredients[index] = ingredient

                it.copy(
                    ingredients = ingredients
                )
            } else it
        }
    }

    fun onRecipeInstructionAdded(instruction: Instruction) {
        recipeState.update {
            it.copy(
                instructions = it.instructions + instruction
            )
        }
    }

    fun onRecipeInstructionRemoved(instruction: Instruction) {
        recipeState.update {
            it.copy(
                instructions = it.instructions - instruction
            )
        }
    }

    fun onRecipeInstructionUpdated(index: Int, instruction: Instruction) {
        recipeState.update {
            if (index in 0..<it.instructions.size) {
                val instructions = it.instructions.toMutableList()

                instructions[index] = instruction

                it.copy(
                    instructions = instructions
                )
            } else it
        }
    }

    private fun createViewRecipeScreenState(
        screenState: ScreenState,
        recipeState: RecipeState
    ) =
        ViewRecipeScreenState(
            selectedTab = screenState.selectedTab,
            isChangeNameDialogOpen = screenState.isChangeNameDialogOpen,
            isDeleteConfirmationDialogOpen = screenState.isDeleteConfirmationDialogOpen,
            isRecipeDeleted = screenState.isRecipeDeleted,
            isEditing = screenState.isEditing,
            recipeName = recipeState.name,
            isRecipeStarred = recipeState.isStarred,
            recipeIngredients = recipeState.ingredients,
            recipeInstructions = recipeState.instructions
        )

    private fun rollbackChanges() {
        if (::editingRecipe.isInitialized) {
            recipeState.update {
                it.copy(
                    name = editingRecipe.name,
                    isStarred = editingRecipe.isStarred,
                    ingredients = editingRecipe.ingredients,
                    instructions = editingRecipe.instructions,
                )
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    @Immutable
    private data class RecipeState(
        val name: String = "",
        val isStarred: Boolean = false,
        val ingredients: List<Ingredient> = emptyList(),
        val instructions: List<Instruction> = emptyList(),
        val createdAt: ZonedDateTime = ZonedDateTime.now(Clock.systemUTC())
    ) {
        fun toRecipe(id: Long = 0) =
            Recipe(
                id = id,
                name = name,
                isStarred = isStarred,
                ingredients = ingredients,
                instructions = instructions,
                createdAt = createdAt,
                lastUpdatedAt = ZonedDateTime.now(Clock.systemUTC())
            )
    }

    @Immutable
    private data class ScreenState(
        val selectedTab: ViewRecipeScreenTab = ViewRecipeScreenTab.INGREDIENTS,
        val isEditing: Boolean = false,
        val isRecipeDeleted: Boolean = false,
        val isChangeNameDialogOpen: Boolean = false,
        val isDeleteConfirmationDialogOpen: Boolean = false
    )
}