package com.nicoapps.cooktime.ui.components.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicoapps.cooktime.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRecipesBar(
    modifier: Modifier = Modifier,
    viewModel: SearchRecipesViewModel = hiltViewModel(),
    onActiveChange: ((Boolean) -> Unit)? = null,
    onRecipeSelected: (Recipe) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    var boxModifier = modifier
        .windowInsetsPadding(
            TopAppBarDefaults.windowInsets.only(
                WindowInsetsSides.Bottom
            )
        )
        .fillMaxWidth()

    if (screenState.isSearchActive) {
        boxModifier = boxModifier.fillMaxHeight()
    }

    Box(modifier = boxModifier) {
        SearchBar(
            modifier = modifier
                .align(Alignment.TopCenter),
            placeholder = placeholder,
            query = screenState.searchText,
            onQueryChange = {
                viewModel.onSearchTextChanged(it)
            },
            onSearch = {
                viewModel.onSearchTextChanged(it)
            },
            active = screenState.isSearchActive,
            onActiveChange = {
                viewModel.onSearchActiveChanged(it)
                onActiveChange?.invoke(it)
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            leadingIcon = leadingIcon
        ) {
            SearchRecipesView(
                recipes = screenState.recipes,
                onIsActiveChanged = viewModel::onSearchActiveChanged,
                onRecipeSelected = { onRecipeSelected(it) }
            )
        }
    }
}