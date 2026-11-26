package com.nicoapps.cooktime.ui.screens.recipe.list.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.data.RecipeRepository
import com.nicoapps.cooktime.model.RecipeSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchRecipesViewModel @Inject constructor(
    @LocalRepository private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val searchText = MutableStateFlow("")

    /**
     * Recipes whose name contains the search text, queried once the user stops typing. A blank
     * text clears the results right away, without querying.
     */
    val results: StateFlow<List<RecipeSummary>> = searchText
        .map { it.trim() }
        .distinctUntilChanged()
        .debounce { query -> if (query.isEmpty()) 0L else DEBOUNCE_MILLIS }
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(emptyList())
            } else {
                recipeRepository.searchByName(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    fun onSearchTextChanged(text: String) {
        searchText.value = text
    }

    private companion object {
        private const val DEBOUNCE_MILLIS = 300L
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
