package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.components.AppTitleTopBar
import com.nicoapps.cooktime.ui.screens.recipe.edit.appbar.ViewRecipeAppBottomBarActions
import com.nicoapps.cooktime.ui.screens.recipe.edit.appbar.ViewRecipeAppBottomBarFab
import com.nicoapps.cooktime.ui.screens.recipe.edit.appbar.ViewRecipeAppTopBarTitle
import com.nicoapps.cooktime.ui.screens.recipe.edit.dialogs.DeleteRecipeAlertDialog
import com.nicoapps.cooktime.ui.screens.recipe.edit.dialogs.EditRecipeNameDialog
import com.nicoapps.cooktime.ui.screens.recipe.edit.tabs.ViewRecipeIngredientsTab
import com.nicoapps.cooktime.ui.screens.recipe.edit.tabs.ViewRecipeInstructionsTab
import com.nicoapps.cooktime.ui.transitions.bottomBarTransition
import com.nicoapps.cooktime.ui.transitions.screenContentTransition
import com.nicoapps.cooktime.ui.transitions.topBarTransition

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewRecipeScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewRecipeViewModel = hiltViewModel(),
    onRecipeDeleted: (recipeId: Long) -> Unit,
    onStartExecution: (recipeId: Long, recipeTitle: String) -> Unit
) {
    val tabs = setOf(
        stringResource(id = R.string.view_recipe_ingredients_tab),
        stringResource(id = R.string.view_recipe_instructions_tab)
    )

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState { tabs.size }
    val errorMessage = screenState.errorMessageResourceId?.let { stringResource(it) }
    val errorSnackbarHostState = remember { SnackbarHostState() }

    val onBackPressedDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher

    LaunchedEffect(screenState.isRecipeDeleted) {
        if (screenState.isRecipeDeleted) {
            onRecipeDeleted(screenState.recipeId)
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            errorSnackbarHostState.showSnackbar(errorMessage)

            viewModel.onErrorShown()
        }
    }

    LaunchedEffect(screenState.selectedTab) {
        pagerState.animateScrollToPage(screenState.selectedTab.index)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            viewModel.onSelectedTabChanged(
                ViewRecipeScreenTab.fromIndex(pagerState.currentPage)
            )
        }
    }

    BackHandler(enabled = screenState.isEditing) {
        viewModel.onFinishEditing(saveChanges = false)
    }

    if (screenState.isDeleteConfirmationDialogOpen) {
        DeleteRecipeAlertDialog(
            onDismissRequest = viewModel::dismissDeleteConfirmationDialog,
            onConfirmed = viewModel::deleteRecipe
        )
    }

    if (screenState.isChangeNameDialogOpen) {
        EditRecipeNameDialog(
            recipeName = screenState.recipeName,
            onDismissRequest = viewModel::dismissChangeNameDialog,
            onConfirmed = {
                viewModel.onRecipeNameChanged(it)
                viewModel.dismissChangeNameDialog()
            }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            AppTitleTopBar(
                modifier = Modifier.topBarTransition(),
                title = {
                    ViewRecipeAppTopBarTitle(
                        titleText = screenState.recipeName,
                        isEditing = screenState.isEditing,
                        onEditClick = viewModel::onEditNameClick
                    )
                },
                onNavigationIconClick = { onBackPressedDispatcher.onBackPressed() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = errorSnackbarHostState)
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.bottomBarTransition(),
                actions = {
                    ViewRecipeAppBottomBarActions(
                        isEditing = screenState.isEditing,
                        isStarred = screenState.isRecipeStarred,
                        isEditDoneEnabled = screenState.canSaveChanges,
                        isSaving = screenState.isSaving,
                        onEditClick = viewModel::onEditClick,
                        onDeleteClick = viewModel::onRecipeDeleteRequest,
                        onStarClick = {
                            viewModel.onRecipeStarredChanged(
                                screenState.isRecipeStarred.not()
                            )
                        },
                        onEditDoneClick = {
                            viewModel.onFinishEditing(saveChanges = true)
                        },
                        onEditCancelClick = {
                            viewModel.onFinishEditing(saveChanges = false)
                        }
                    )
                },
                floatingActionButton = {
                    ViewRecipeAppBottomBarFab(
                        isVisible = screenState.isEditing.not(),
                        onClick = {
                            onStartExecution(screenState.recipeId, screenState.recipeName)
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .screenContentTransition()
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(pagerState.currentPage)
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
                                ViewRecipeScreenTab.INGREDIENTS.index -> {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ingredients_icon),
                                        contentDescription = null
                                    )
                                }

                                ViewRecipeScreenTab.INSTRUCTIONS.index -> {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.List,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        selected = screenState.selectedTab.index == index,
                        onClick = {
                            viewModel.onSelectedTabChanged(
                                ViewRecipeScreenTab.fromIndex(index)
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
                when (ViewRecipeScreenTab.fromIndex(page)) {
                    ViewRecipeScreenTab.INGREDIENTS -> {
                        ViewRecipeIngredientsTab(
                            modifier = Modifier.padding(top = 10.dp),
                            isEditing = screenState.isEditing,
                            ingredients = screenState.recipeDraft.ingredients,
                            onIngredientAdded = { name, quantity, measurementUnit ->
                                viewModel.onRecipeIngredientAdded(
                                    name,
                                    quantity,
                                    measurementUnit
                                )
                            },
                            onIngredientUpdated = { key, name, quantity, measurementUnit ->
                                viewModel.onRecipeIngredientUpdated(
                                    key,
                                    name,
                                    quantity,
                                    measurementUnit
                                )
                            },
                            onIngredientRemoved = { viewModel.onRecipeIngredientRemoved(it) },
                        )
                    }

                    ViewRecipeScreenTab.INSTRUCTIONS -> {
                        ViewRecipeInstructionsTab(
                            modifier = Modifier.padding(top = 10.dp),
                            isEditing = screenState.isEditing,
                            recipeInstructions = screenState.recipeDraft.instructions,
                            onInstructionAdded = { viewModel.onRecipeInstructionAdded(it) },
                            onInstructionUpdated = { key, text ->
                                viewModel.onRecipeInstructionUpdated(key, text)
                            },
                            onInstructionRemoved = { viewModel.onRecipeInstructionRemoved(it) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewRecipeScreenPreview() {
    ViewRecipeScreen(
        onRecipeDeleted = {},
        onStartExecution = { _, _ -> }
    )
}
