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
    private val _screenState = MutableStateFlow(ViewRecipeScreenState())

    private var recipeStateBeforeChanges = RecipeState()

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
        screenState.copy(
            recipeInstructions = recipeState.instructions,
            recipeIngredients = recipeState.ingredients,
            recipeName = recipeState.name,
            isRecipeStarred = recipeState.isStarred
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
        viewModelScope.launch {
            recipeRepository.deleteById(recipeId)

            _screenState.update {
                it.copy(
                    isDeleteConfirmationDialogOpen = false,
                    isRecipeDeleted = confirmed
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

        recipeStateBeforeChanges = recipeState.value
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
                    Recipe(
                        id = recipeId,
                        name = recipeState.value.name,
                        ingredients = recipeState.value.ingredients,
                        instructions = recipeState.value.instructions,
                        isStarred = recipeState.value.isStarred,
                        createdAt = recipeState.value.createdAt,
                        lastUpdatedAt = ZonedDateTime.now(Clock.systemUTC())
                    )
                )
            }
        } else {
            recipeState.update {
                it.copy(
                    name = recipeStateBeforeChanges.name,
                    isStarred = recipeStateBeforeChanges.isStarred,
                    ingredients = recipeStateBeforeChanges.ingredients,
                    instructions = recipeStateBeforeChanges.instructions,
                )
            }
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
    )

    @Immutable
    data class ViewRecipeScreenState(
        val selectedTab: ViewRecipeScreenTab = ViewRecipeScreenTab.INGREDIENTS,
        val recipeName: String = "",
        val isEditing: Boolean = false,
        val isRecipeDeleted: Boolean = false,
        val isRecipeStarred: Boolean = false,
        val isChangeNameDialogOpen: Boolean = false,
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