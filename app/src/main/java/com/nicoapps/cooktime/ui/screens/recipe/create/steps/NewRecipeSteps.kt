package com.nicoapps.cooktime.ui.screens.recipe.create.steps

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.ui.defaultAnimationSpec
import com.nicoapps.cooktime.ui.screens.recipe.create.NewRecipeScreenStep

@Composable
fun NewRecipeSteps(
    modifier: Modifier = Modifier,
    currentStep: NewRecipeScreenStep,
    recipeName: String,
    recipeIngredients: List<Ingredient>,
    recipeInstructions: List<Instruction>,
    currentTitle: String,
    nextButtonEnabled: Boolean,
    onNextClicked: () -> Unit,
    onFinishClicked: () -> Unit,
    onCurrentTitleChanged: (String) -> Unit,
    onRecipeNameChanged: (String) -> Unit,
    onRecipeIngredientAdded: (Ingredient) -> Unit,
    onRecipeIngredientUpdated: (Int, Ingredient) -> Unit,
    onRecipeIngredientRemoved: (Ingredient) -> Unit,
    onRecipeInstructionAdded: (Instruction) -> Unit,
    onRecipeInstructionUpdated: (Int, Instruction) -> Unit,
    onRecipeInstructionRemoved: (Instruction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.header_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedContent(
            modifier = modifier
                .fillMaxHeight(.9f),
            targetState = currentStep,
            label = "newRecipeStepChangedAnimation",
            transitionSpec = {
                if (initialState.stepIndex > targetState.stepIndex) {
                    slideInHorizontally(
                        animationSpec = defaultAnimationSpec()
                    ) { width -> -width } + fadeIn(
                        animationSpec = defaultAnimationSpec()
                    ) togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                } else {
                    slideInHorizontally(
                        animationSpec = defaultAnimationSpec()
                    ) { width -> width } + fadeIn(
                        animationSpec = defaultAnimationSpec()
                    ) togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut(
                        animationSpec = defaultAnimationSpec()
                    )
                }.using(
                    SizeTransform(clip = true)
                )
            }
        ) { currentStep ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = modifier,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    text = currentTitle
                )

                when (currentStep) {
                    NewRecipeScreenStep.NAME -> {
                        NewRecipeNameStep(
                            modifier = modifier,
                            recipeName = recipeName,
                            onCurrentTitleChanged = { onCurrentTitleChanged(it) },
                            onRecipeNameChanged = { onRecipeNameChanged(it) }
                        )
                    }

                    NewRecipeScreenStep.INGREDIENTS -> {
                        NewRecipeIngredientsStep(
                            modifier = modifier,
                            onCurrentTitleChanged = { onCurrentTitleChanged(it) },
                            ingredients = recipeIngredients,
                            onIngredientAdded = { onRecipeIngredientAdded(it) },
                            onIngredientUpdated = { index, ingredient ->
                                onRecipeIngredientUpdated(index, ingredient)
                            },
                            onIngredientRemoved = { onRecipeIngredientRemoved(it) },
                        )
                    }

                    NewRecipeScreenStep.INSTRUCTIONS -> {
                        NewRecipeInstructionsStep(
                            modifier = modifier,
                            instructions = recipeInstructions,
                            onCurrentTitleChanged = { onCurrentTitleChanged(it) },
                            onInstructionAdded = { onRecipeInstructionAdded(it) },
                            onInstructionUpdated = { index, value ->
                                onRecipeInstructionUpdated(index, value)
                            },
                            onInstructionRemoved = { onRecipeInstructionRemoved(it) }
                        )
                    }
                }
            }
        }

        AnimatedContent(
            modifier = Modifier
                .align(Alignment.End),
            targetState = currentStep,
            label = "newRecipeStepButtonAnimation",
            transitionSpec = {
                if (initialState.isLastStep() != targetState.isLastStep()) {
                    scaleIn() togetherWith scaleOut()
                } else {
                    ContentTransform(EnterTransition.None, ExitTransition.None)
                }.using(
                    SizeTransform(clip = true)
                )
            }
        ) { currentButtonStep ->
            val buttonModifier = Modifier
                .fillMaxWidth(.5f)
                .padding(dimensionResource(id = R.dimen.new_recipe_step_button_padding))

            if (currentButtonStep.isLastStep()) {
                Button(
                    modifier = buttonModifier,
                    enabled = nextButtonEnabled,
                    onClick = { onFinishClicked() }
                ) {
                    Text(
                        text = stringResource(id = R.string.new_recipe_name_step_finish)
                    )
                }
            } else {
                FilledTonalButton(
                    modifier = buttonModifier,
                    enabled = nextButtonEnabled,
                    onClick = { onNextClicked() }
                ) {
                    Text(
                        text = stringResource(id = R.string.new_recipe_name_step_next)
                    )
                }
            }
        }
    }
}