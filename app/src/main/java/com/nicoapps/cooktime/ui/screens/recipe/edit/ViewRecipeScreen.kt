package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavGraphBottomBarState
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavGraphTopBarContentType
import com.nicoapps.cooktime.ui.AppNavGraphTopBarState
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.components.AppTabIndicator
import com.nicoapps.cooktime.ui.dialogSurfaceColor
import com.nicoapps.cooktime.ui.dialogTextFieldColors
import com.nicoapps.cooktime.ui.screens.recipe.tabs.ViewRecipeIngredientsTab
import com.nicoapps.cooktime.ui.screens.recipe.tabs.ViewRecipeInstructionsTab

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewRecipeScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewRecipeViewModel = hiltViewModel(),
    appNavigationActions: AppNavigationActions,
    onComposing: (AppNavGraphState) -> Unit
) {
    val tabs = setOf(
        stringResource(id = R.string.view_recipe_ingredients_tab),
        stringResource(id = R.string.view_recipe_instructions_tab)
    )

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState { tabs.size }

    LaunchedEffect(screenState) {
        if (screenState.isRecipeDeleted) {
            appNavigationActions.navigateBack()
        } else {
            onComposing(
                AppNavGraphState(
                    topBar = AppNavGraphTopBarState(
                        contentType = AppNavGraphTopBarContentType.TITLE_ONLY,
                        showActions = screenState.isEditing,
                        title = screenState.recipeName,
                        actions = {
                            ViewRecipeAppTitleTopBarActions(
                                onDoneClick = { viewModel.onFinishEditing(saveChanges = true) },
                                onCancelClick = { viewModel.onFinishEditing(saveChanges = false) },
                                onChangeNameClick = { viewModel.onEditNameClick() }
                            )
                        }
                    ),
                    bottomBar = AppNavGraphBottomBarState(
                        visible = screenState.isEditing.not(),
                        actions = {
                            IconButton(onClick = { viewModel.onEditClick() }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Localized description",
                                )
                            }

                            IconButton(onClick = {
                                viewModel.onRecipeDeleteRequest()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Localized description",
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.onRecipeStarredChanged(screenState.isRecipeStarred.not())
                                }) {
                                Icon(
                                    imageVector =
                                    if (screenState.isRecipeStarred)
                                        Icons.Default.Favorite
                                    else
                                        Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Localized description",
                                )
                            }
                        },
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                onClick = { },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                                text = {
                                    Text(text = stringResource(id = R.string.start_recipe_button_text))
                                },
                                icon = {
                                    Icon(Icons.Default.PlayArrow, "Localized description")
                                }
                            )
                        }
                    )
                )
            )
        }
    }

    LaunchedEffect(screenState.selectedTab) {
        pagerState.animateScrollToPage(screenState.selectedTab.index)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            viewModel.onSelectedTabChanged(
                ViewRecipeViewModel.ViewRecipeScreenTab.fromIndex(pagerState.currentPage)
            )
        }
    }

    BackHandler(enabled = screenState.isEditing) {
        viewModel.onFinishEditing(saveChanges = false)
    }

    if (screenState.isDeleteConfirmationDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissDeleteConfirmationDialog(confirmed = false)
            },
            title = {
                Text(
                    text = stringResource(
                        id = R.string.view_recipe_delete_confirmation_dialog_title
                    )
                )
            },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.view_recipe_delete_confirmation_dialog_description
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissDeleteConfirmationDialog(confirmed = true)
                }) {
                    Text(
                        text = stringResource(
                            id = R.string.view_recipe_delete_confirmation_dialog_confirm
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.dismissDeleteConfirmationDialog(confirmed = false)
                }) {
                    Text(
                        text = stringResource(
                            id = R.string.view_recipe_delete_confirmation_dialog_cancel
                        )
                    )
                }
            }
        )
    }

    if (screenState.isChangeNameDialogOpen) {

        Dialog(
            onDismissRequest = {
                viewModel.dismissChangeNameDialog()
            }
        ) {
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = dialogSurfaceColor()
                )
            ) {
                var recipeName by remember { mutableStateOf(screenState.recipeName) }

                Text(
                    modifier = Modifier.padding(
                        dimensionResource(id = R.dimen.dialog_title_spacing)
                    ),
                    text = stringResource(id = R.string.change_recipe_name_dialog_title),
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.text_input_padding)),
                    placeholder = {
                        Text(
                            text = stringResource(
                                R.string.change_recipe_name_placeholder
                            )
                        )
                    },
                    colors = dialogTextFieldColors(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    singleLine = true,
                    value = recipeName,
                    onValueChange = {
                        recipeName = it
                    }
                )

                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        modifier = Modifier.padding(
                            dimensionResource(id = R.dimen.dialog_buttons_spacing)
                        ),
                        onClick = { viewModel.dismissChangeNameDialog() }
                    ) {
                        Text(
                            text = stringResource(id = R.string.new_recipe_add_ingredient_cancel)
                        )
                    }

                    TextButton(
                        modifier = Modifier.padding(
                            dimensionResource(id = R.dimen.dialog_buttons_spacing)
                        ),
                        enabled = recipeName.isNotBlank(),
                        onClick = {
                            viewModel.onRecipeNameChanged(recipeName)
                            viewModel.dismissChangeNameDialog()
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.new_recipe_add_ingredient_done)
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = {
                AppTabIndicator(
                    pagerState = pagerState,
                    tabPositions = it
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    text = {
                        Text(
                            text = tab
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.inversePrimary,
                    icon = {
                        when (index) {
                            ViewRecipeViewModel.ViewRecipeScreenTab.INGREDIENTS.index -> {
                                Icon(
                                    painter = painterResource(id = R.drawable.ingredients_icon),
                                    contentDescription = null
                                )
                            }

                            ViewRecipeViewModel.ViewRecipeScreenTab.INSTRUCTIONS.index -> {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    selected = screenState.selectedTab.index == index,
                    onClick = {
                        viewModel.onSelectedTabChanged(
                            ViewRecipeViewModel.ViewRecipeScreenTab.fromIndex(index)
                        )
                    }
                )
            }
        }

        HorizontalPager(
            modifier = Modifier
                .weight(1f),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            when (ViewRecipeViewModel.ViewRecipeScreenTab.fromIndex(page)) {
                ViewRecipeViewModel.ViewRecipeScreenTab.INGREDIENTS -> {
                    ViewRecipeIngredientsTab(
                        modifier = Modifier.padding(top = 10.dp),
                        isEditing = screenState.isEditing,
                        ingredients = screenState.recipeIngredients,
                        onIngredientAdded = { viewModel.onRecipeIngredientAdded(it) },
                        onIngredientUpdated = { index, ingredient ->
                            viewModel.onRecipeIngredientUpdated(index, ingredient)
                        },
                        onIngredientRemoved = { viewModel.onRecipeIngredientRemoved(it) },
                    )
                }

                ViewRecipeViewModel.ViewRecipeScreenTab.INSTRUCTIONS -> {
                    ViewRecipeInstructionsTab(
                        modifier = Modifier.padding(top = 10.dp),
                        recipeInstructions = screenState.recipeInstructions
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewRecipeScreenPreview() {
    ViewRecipeScreen(
        onComposing = {},
        appNavigationActions = AppNavigationActions(rememberNavController())
    )
}