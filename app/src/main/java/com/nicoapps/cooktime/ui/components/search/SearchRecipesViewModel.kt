package com.nicoapps.cooktime.ui.components.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchRecipesViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _recipes = recipeRepository.getAll()
    private val _searchState = MutableStateFlow(SearchState())

    val screenState = _searchState.combine(_recipes) { searchState, recipes ->
        var filteredRecipes = emptyList<Recipe>()

        if (searchState.searchText.isNotBlank()) {
            filteredRecipes = recipes.filter {
                it.name.contains(searchState.searchText, ignoreCase = true)
            }
        }

        SearchRecipesScreenState(
            recipes = filteredRecipes,
            searchText = searchState.searchText,
            isSearchActive = searchState.isSearchActive
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        SearchRecipesScreenState()
    )

    fun onSearchTextChanged(searchText: String) =
        _searchState.update {
            it.copy(
                searchText = searchText
            )
        }

    fun onSearchActiveChanged(isSearchActive: Boolean) =
        _searchState.update {
            it.copy(
                isSearchActive = isSearchActive
            )
        }

    data class SearchRecipesScreenState(
        val recipes: List<Recipe> = emptyList(),
        val searchText: String = "",
        val isSearchActive: Boolean = false
    )

    private data class SearchState(
        val searchText: String = "",
        val isSearchActive: Boolean = false
    )
}