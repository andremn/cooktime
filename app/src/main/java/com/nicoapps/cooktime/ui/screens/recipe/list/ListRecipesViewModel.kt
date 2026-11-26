package com.nicoapps.cooktime.ui.screens.recipe.list

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.Recipe
import com.nicoapps.cooktime.ui.extensions.runWrite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val errorMessageResourceId = MutableStateFlow<Int?>(null)

    val screenState = combine(
        recipeRepository.getAll(),
        errorMessageResourceId
    ) { recipes, errorMessageResourceId ->
        HomeScreenState(
            recipes = recipes,
            errorMessageResourceId = errorMessageResourceId
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        HomeScreenState()
    )

    fun onStarChanged(recipeId: Long, isStarred: Boolean) {
        viewModelScope.launch {
            runWrite(
                onFailure = {
                    errorMessageResourceId.value = R.string.error_update_recipe_star
                }
            ) {
                recipeRepository.updateIsStarred(
                    id = recipeId,
                    isStarred = isStarred
                )
            }
        }
    }

    fun onErrorShown() {
        errorMessageResourceId.value = null
    }
}

@Immutable
data class HomeScreenState(
    val recipes: List<Recipe> = emptyList(),
    @StringRes val errorMessageResourceId: Int? = null
)
