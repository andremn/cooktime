package com.nicoapps.cooktime.ui.screens.recipe

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavGraphTopBarContentType
import com.nicoapps.cooktime.ui.AppNavGraphTopBarState
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.components.AppSnackbarState
import com.nicoapps.cooktime.ui.screens.recipe.steps.NewRecipeSteps
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Composable
fun NewRecipeScreen(
    modifier: Modifier = Modifier,
    viewModel: NewRecipeViewModel = hiltViewModel(),
    appNavigationActions: AppNavigationActions,
    appSnackbarState: AppSnackbarState,
    onComposing: (AppNavGraphState) -> Unit
) {
    val context = LocalContext.current
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val animatedProgress by animateFloatAsState(
        label = "newRecipeProgressAnimation",
        targetValue = screenState.currentProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    LaunchedEffect(Unit) {
        onComposing(
            AppNavGraphState(
                topBar = AppNavGraphTopBarState(
                    contentType = AppNavGraphTopBarContentType.TITLE_ONLY,
                    title = context.resources.getString(R.string.new_recipe_title)
                )
            )
        )
    }

    LaunchedEffect(screenState.currentStep) {
        if (!screenState.currentStep.isFistStep()) {
            onComposing(
                AppNavGraphState(
                    topBar = AppNavGraphTopBarState(
                        contentType = AppNavGraphTopBarContentType.TITLE_ONLY,
                        title = screenState.recipeName.ifBlank {
                            context.resources.getString(R.string.new_recipe_title)
                        }
                    )
                )
            )
        }
    }

    LaunchedEffect(screenState.recipeSaved) {
        if (screenState.recipeSaved) {
            appSnackbarState.show(
                context.resources.getString(R.string.new_recipe_saved_message)
            )

            appNavigationActions.navigateToHome()
        }
    }

    BackHandler(!screenState.currentStep.isFistStep()) {
        viewModel.previousStep()
    }

    Column(
        modifier = modifier
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
            nextButtonEnabled = screenState.nextButtonEnabled,
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

@Preview(showBackground = true)
@Composable
fun NewRecipeScreenPreview() {
    NewRecipeScreen(
        appNavigationActions = AppNavigationActions(NavHostController(LocalContext.current)),
        appSnackbarState = AppSnackbarState(
            SnackbarHostState(),
            CoroutineScope(Dispatchers.Default)
        ),
        onComposing = {}
    )
}