package com.nicoapps.cooktime.ui.screens.recipe.create

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.nicoapps.cooktime.ui.transitions.SharedElementKey
import com.nicoapps.cooktime.ui.transitions.sharedContainer
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
    val title = if (screenState.currentStep.isFirstStep()) {
        stringResource(R.string.new_recipe_title)
    } else {
        screenState.draft.name.ifBlank { stringResource(R.string.new_recipe_title) }
    }
    val recipeSavedMessage = stringResource(R.string.new_recipe_saved_message)
    val errorMessage = screenState.errorMessageResourceId?.let { stringResource(it) }
    val errorSnackbarHostState = remember { SnackbarHostState() }

    val onBackPressedDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher

    LaunchedEffect(screenState.recipeSaved) {
        if (screenState.recipeSaved) {
            appSnackbarState.show(recipeSavedMessage)

            appNavigationActions.navigateToHome()
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            errorSnackbarHostState.showSnackbar(errorMessage)

            viewModel.onErrorShown()
        }
    }

    BackHandler(screenState.backHandlerEnabled) {
        viewModel.previousStep()
    }

    Scaffold(
        modifier = modifier.sharedContainer(
            key = SharedElementKey.NewRecipeContainer,
            clipShape = FloatingActionButtonDefaults.shape
        ),
        topBar = {
            AppTitleTopBar(
                title = {
                    Text(text = title)
                },
                onNavigationIconClick = { onBackPressedDispatcher.onBackPressed() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = errorSnackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding()
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
                draft = screenState.draft,
                currentStep = screenState.currentStep,
                nextButtonEnabled = screenState.nextButtonEnabled,
                onNextClicked = { viewModel.nextStep() },
                onFinishClicked = { viewModel.saveRecipe() },
                onRecipeNameChanged = { viewModel.onRecipeNameChanged(it) },
                onRecipeIngredientAdded = { name, quantity, measurementUnit ->
                    viewModel.onRecipeIngredientAdded(name, quantity, measurementUnit)
                },
                onRecipeIngredientUpdated = { key, name, quantity, measurementUnit ->
                    viewModel.onRecipeIngredientUpdated(key, name, quantity, measurementUnit)
                },
                onRecipeIngredientRemoved = { viewModel.onRecipeIngredientRemoved(it) },
                onRecipeInstructionAdded = { viewModel.onRecipeInstructionAdded(it) },
                onRecipeInstructionUpdated = { key, text ->
                    viewModel.onRecipeInstructionUpdated(key, text)
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
