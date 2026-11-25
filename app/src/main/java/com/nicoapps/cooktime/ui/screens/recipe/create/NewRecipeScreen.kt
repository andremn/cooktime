package com.nicoapps.cooktime.ui.screens.recipe.create

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.components.AppSnackbarState
import com.nicoapps.cooktime.ui.components.AppTitleTopBar
import com.nicoapps.cooktime.ui.screens.recipe.create.steps.NewRecipeSteps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun NewRecipeScreen(
    modifier: Modifier = Modifier,
    viewModel: NewRecipeViewModel = hiltViewModel(),
    appNavigationActions: AppNavigationActions,
    appSnackbarState: AppSnackbarState
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val animatedProgress by animateFloatAsState(
        label = "newRecipeProgressAnimation",
        targetValue = screenState.currentProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    val title = if (screenState.currentStep.isFistStep()) {
        stringResource(R.string.new_recipe_title)
    } else {
        screenState.recipeName.ifBlank { stringResource(R.string.new_recipe_title) }
    }
    val recipeSavedMessage = stringResource(R.string.new_recipe_saved_message)

    val onBackPressedDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher

    LaunchedEffect(screenState.recipeSaved) {
        if (screenState.recipeSaved) {
            appSnackbarState.show(recipeSavedMessage)

            appNavigationActions.navigateToHome()
        }
    }

    BackHandler(!screenState.currentStep.isFistStep()) {
        viewModel.previousStep()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTitleTopBar(
                title = {
                    Text(text = title)
                },
                onNavigationIconClick = { onBackPressedDispatcher.onBackPressed() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            LinearProgressIndicator(
                progress = {
                    animatedProgress
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
            )

            NewRecipeSteps(
                recipeName = screenState.recipeName,
                recipeIngredients = screenState.recipeIngredients,
                recipeInstructions = screenState.recipeInstructions,
                currentTitle = screenState.currentTitle,
                currentStep = screenState.currentStep,
                nextButtonEnabled = screenState.nextButtonEnabled && !screenState.isSaving,
                onNextClicked = { viewModel.nextStep() },
                onFinishClicked = { viewModel.saveRecipe() },
                onCurrentTitleChanged = { viewModel.onCurrentTitleChanged(it) },
                onRecipeNameChanged = { viewModel.onRecipeNameChanged(it) },
                onRecipeIngredientAdded = { viewModel.onRecipeIngredientAdded(it) },
                onRecipeIngredientUpdated = { index, ingredient ->
                    viewModel.onRecipeIngredientUpdated(index, ingredient)
                },
                onRecipeIngredientRemoved = { viewModel.onRecipeIngredientRemoved(it) },
                onRecipeInstructionAdded = { viewModel.onRecipeInstructionAdded(it) },
                onRecipeInstructionUpdated = { index, instruction ->
                    viewModel.onRecipeInstructionUpdated(index, instruction)
                },
                onRecipeInstructionRemoved = { viewModel.onRecipeInstructionRemoved(it) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewRecipeScreenPreview() {
    NewRecipeScreen(
        appNavigationActions = AppNavigationActions(NavHostController(LocalContext.current)),
        appSnackbarState = AppSnackbarState(
            SnackbarHostState(),
            CoroutineScope(Dispatchers.Default)
        )
    )
}
