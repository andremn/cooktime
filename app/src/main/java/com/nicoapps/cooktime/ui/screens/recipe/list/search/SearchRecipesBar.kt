package com.nicoapps.cooktime.ui.screens.recipe.list.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicoapps.cooktime.model.RecipeSummary
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
fun SearchRecipesBar(
    modifier: Modifier = Modifier,
    viewModel: SearchRecipesViewModel = hiltViewModel(),
    onActiveChange: ((Boolean) -> Unit)? = null,
    onRecipeSelected: (RecipeSummary) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val results by viewModel.results.collectAsStateWithLifecycle()
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val coroutineScope = rememberCoroutineScope()
    val currentOnActiveChange by rememberUpdatedState(onActiveChange)

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text.toString() }
            .collect { viewModel.onSearchTextChanged(it) }
    }

    LaunchedEffect(searchBarState) {
        snapshotFlow { searchBarState.currentValue == SearchBarValue.Expanded }
            .distinctUntilChanged()
            .drop(1)
            .collect { isActive ->
                // The text is kept only while the search is open: once it closes, the bar shows
                // the placeholder again.
                if (!isActive) {
                    textFieldState.clearText()
                }

                currentOnActiveChange?.invoke(isActive)
            }
    }

    val colors = SearchBarDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )

    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = { viewModel.onSearchTextChanged(it) },
            placeholder = placeholder,
            leadingIcon = leadingIcon
        )
    }

    Box(
        modifier = modifier
            .windowInsetsPadding(SearchBarDefaults.windowInsets)
            .fillMaxWidth()
    ) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            state = searchBarState,
            inputField = inputField,
            colors = colors
        )
    }

    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
        colors = colors
    ) {
        SearchRecipesView(
            recipes = results,
            onRecipeSelected = { recipe ->
                coroutineScope.launch { searchBarState.animateToCollapsed() }
                onRecipeSelected(recipe)
            }
        )
    }
}
