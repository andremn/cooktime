package com.nicoapps.cooktime.ui.screens.recipe.list

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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavGraphFloatingActionButtonState
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavGraphTopBarContentType
import com.nicoapps.cooktime.ui.AppNavGraphTopBarState
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.components.recipe.StarredRecipeIcon

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    appNavigationActions: AppNavigationActions,
    onComposing: (AppNavGraphState) -> Unit
) {
    val context = LocalContext.current

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onComposing(
            AppNavGraphState(
                floatingActionButton = AppNavGraphFloatingActionButtonState(
                    visible = true,
                    content = {
                        Icon(
                            Icons.Filled.Add,
                            stringResource(id = R.string.new_recipe_button_text)
                        )
                    },
                    onClick = { appNavigationActions.navigateToNewRecipe() }
                ),
                topBar = AppNavGraphTopBarState(
                    contentType = AppNavGraphTopBarContentType.SEARCH_BAR,
                    searchBarPlaceholder = {
                        Text(
                            text = context.getString(R.string.search_recipes_placeholder)
                        )
                    }
                )
            )
        )
    }

    if (screenState.recipes.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
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
            modifier = modifier
                .fillMaxSize()
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