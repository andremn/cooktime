package com.nicoapps.cooktime.ui.screens.recipe.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.nicoapps.cooktime.ui.AppNavGraphTopBarState
import com.nicoapps.cooktime.ui.AppNavigationActions

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
                        FloatingActionButton(
                            onClick = {
                                appNavigationActions.navigateToNewRecipe()
                            },
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                stringResource(id = R.string.new_recipe_button_text)
                            )
                        }
                    }
                ),
                topBar = AppNavGraphTopBarState(
                    title = context.resources.getString(
                        R.string.search_recipes_placeholder
                    )
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            items(screenState.recipes) { recipe ->
                key(recipe.id) {
                    ElevatedCard(
                        modifier = Modifier
                            .size(width = 120.dp, height = 200.dp)
                            .padding(vertical = 5.dp)
                            .clickable {
                                appNavigationActions.navigateToViewRecipe(recipe)
                            }
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = recipe.name
                        )
                    }
                }
            }
        }
    }
}