package com.nicoapps.cooktime.ui.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.SearchBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRecipesBar(
    modifier: Modifier = Modifier,
    onActiveChange: ((Boolean) -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val searchBarState = remember { SearchBarState() }

    var boxModifier = modifier
        .windowInsetsPadding(
            TopAppBarDefaults.windowInsets.only(
                WindowInsetsSides.Bottom
            )
        )
        .fillMaxWidth()

    if (searchBarState.isActive) {
        boxModifier = boxModifier.fillMaxHeight()
    }

    Box(modifier = boxModifier) {
        SearchBar(
            modifier = modifier
                .align(Alignment.TopCenter),
            placeholder = placeholder,
            query = searchBarState.searchText,
            onQueryChange = {
                searchBarState.onSearchTextChanged(it)
            },
            onSearch = {
                searchBarState.onSearchTextChanged(it)
            },
            active = searchBarState.isActive,
            onActiveChange = {
                onActiveChange?.invoke(it)
                searchBarState.onIsActiveChanged(it)
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            leadingIcon = leadingIcon
        ) {
            SearchRecipesView(
                searchBarState = searchBarState
            )
        }
    }
}