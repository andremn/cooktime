package com.nicoapps.cooktime.ui.screens.recipe.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.DEFAULT_ANIMATION_DURATION_MILLIS
import com.nicoapps.cooktime.ui.components.AppSnackbarState
import com.nicoapps.cooktime.ui.components.recipe.StarredRecipeIcon
import com.nicoapps.cooktime.ui.components.search.SearchRecipesBar
import com.nicoapps.cooktime.ui.defaultAnimationSpec
import com.nicoapps.cooktime.ui.defaultExitAnimation

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    appNavigationActions: AppNavigationActions,
    appSnackbarState: AppSnackbarState,
    onOpenDrawer: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    var isSearchBarActive by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = appSnackbarState.snackbarHostState)
        },
        topBar = {
            SearchRecipesBar(
                modifier = Modifier.padding(
                    bottom = dimensionResource(id = R.dimen.search_bar_padding)
                ),
                onActiveChange = { isSearchBarActive = it },
                onRecipeSelected = { appNavigationActions.navigateToViewRecipe(it) },
                placeholder = {
                    Text(text = stringResource(R.string.search_recipes_placeholder))
                },
                leadingIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.cd_open_menu)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isSearchBarActive,
                enter = slideInVertically(
                    animationSpec = defaultAnimationSpec(
                        delayMillis = DEFAULT_ANIMATION_DURATION_MILLIS
                    )
                ) { height -> height * 3 },
                exit = defaultExitAnimation()
            ) {
                FloatingActionButton(
                    onClick = { appNavigationActions.navigateToNewRecipe() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.cd_new_recipe)
                    )
                }
            }
        }
    ) { paddingValues ->
        if (screenState.recipes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.home_recipes_empty),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 10.dp)
            ) {
                items(screenState.recipes) { recipe ->
                    key(recipe.id) {
                        ElevatedCard(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clickable {
                                    appNavigationActions.navigateToViewRecipe(recipe)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp)
                            ) {
                                Text(
                                    text = recipe.name,
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Row {
                                    IconButton(
                                        onClick = {
                                            viewModel.onStarChanged(
                                                recipe.id,
                                                recipe.isStarred.not()
                                            )
                                        }
                                    ) {
                                        StarredRecipeIcon(
                                            isStarred = recipe.isStarred
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
