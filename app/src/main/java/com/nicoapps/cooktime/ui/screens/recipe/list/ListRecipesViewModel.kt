package com.nicoapps.cooktime.ui.screens.recipe.list

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository,
    private val databaseCoroutineScope: CoroutineScope
) : ViewModel() {
    val screenState = recipeRepository.getAll().map {
        HomeScreenState(
            recipes = it
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        HomeScreenState()
    )

    fun onStarChanged(recipeId: Long, isStarred: Boolean) {
        databaseCoroutineScope.launch {
            recipeRepository.updateIsStarred(
                id = recipeId,
                isStarred = isStarred
            )
        }
    }
}

@Immutable
data class HomeScreenState(
    val recipes: List<Recipe> = emptyList()
)