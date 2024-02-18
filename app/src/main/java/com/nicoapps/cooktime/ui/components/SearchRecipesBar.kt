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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRecipesBar(
    modifier: Modifier = Modifier,
    onActiveChange: ((Boolean) -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    var boxModifier = modifier
        .windowInsetsPadding(
            TopAppBarDefaults.windowInsets.only(
                WindowInsetsSides.Bottom
            )
        )
        .fillMaxWidth()

    if (isSearchActive) {
        println("HERE!!")
        boxModifier = boxModifier.fillMaxHeight()
    }

    Box(modifier = boxModifier) {
        SearchBar(
            modifier = modifier
                .align(Alignment.TopCenter),
            placeholder = placeholder,
            query = searchText,
            onQueryChange = {
                searchText = it
            },
            onSearch = {
                searchText = it
            },
            active = isSearchActive,
            onActiveChange = {
                onActiveChange?.invoke(it)
                isSearchActive = it
            },
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            leadingIcon = leadingIcon
        ) {
            SearchRecipesView(
                onSearchTextChanged = { searchText = it },
                onIsActiveChanged = { isSearchActive = it }
            )
        }
    }
}