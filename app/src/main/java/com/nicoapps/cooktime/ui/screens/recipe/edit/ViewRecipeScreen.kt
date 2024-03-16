package com.nicoapps.cooktime.ui.screens.recipe.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavGraphBottomBarState
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavGraphTopBarContentType
import com.nicoapps.cooktime.ui.AppNavGraphTopBarState
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.DEFAULT_ANIMATION_DURATION_MILLIS
import com.nicoapps.cooktime.ui.components.AppTabIndicator
import com.nicoapps.cooktime.ui.screens.recipe.edit.appbar.ViewRecipeAppBottomBarActions
import com.nicoapps.cooktime.ui.screens.recipe.edit.appbar.ViewRecipeAppBottomBarFab
import com.nicoapps.cooktime.ui.screens.recipe.edit.appbar.ViewRecipeAppTopBarTitle
import com.nicoapps.cooktime.ui.screens.recipe.edit.dialogs.DeleteRecipeAlertDialog
import com.nicoapps.cooktime.ui.screens.recipe.edit.dialogs.EditRecipeNameDialog
import com.nicoapps.cooktime.ui.screens.recipe.edit.tabs.ViewRecipeIngredientsTab
import com.nicoapps.cooktime.ui.screens.recipe.edit.tabs.ViewRecipeInstructionsTab
import kotlin.time.Duration.Companion.milliseconds

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
                        title = {
                            ViewRecipeAppTopBarTitle(
                                titleText = screenState.recipeName,
                                isEditing = screenState.isEditing,
                                onEditClick = viewModel::onEditNameClick
                            )
                        },
                    ),
                    bottomBar = AppNavGraphBottomBarState(
                        visible = true,
                        delayToBecomeVisible = DEFAULT_ANIMATION_DURATION_MILLIS.milliseconds,
                        actions = {
                            ViewRecipeAppBottomBarActions(
                                isEditing = screenState.isEditing,
                                isStarred = screenState.isRecipeStarred,
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
                                onClick = {}
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
                        ingredients = screenState.recipeIngredients,
                        onIngredientAdded = viewModel::onRecipeIngredientAdded,
                        onIngredientUpdated = viewModel::onRecipeIngredientUpdated,
                        onIngredientRemoved = viewModel::onRecipeIngredientRemoved,
                    )
                }

                ViewRecipeScreenTab.INSTRUCTIONS -> {
                    ViewRecipeInstructionsTab(
                        modifier = Modifier.padding(top = 10.dp),
                        isEditing = screenState.isEditing,
                        recipeInstructions = screenState.recipeInstructions,
                        onInstructionAdded = viewModel::onRecipeInstructionAdded,
                        onInstructionUpdated = viewModel::onRecipeInstructionUpdated,
                        onInstructionRemoved = viewModel::onRecipeInstructionRemoved
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