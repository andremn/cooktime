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
import com.nicoapps.cooktime.ui.defaultAnimationSpec
import com.nicoapps.cooktime.ui.screens.recipe.create.NewRecipeScreenStep
import com.nicoapps.cooktime.ui.screens.recipe.draft.RecipeDraft

@Composable
fun NewRecipeSteps(
    modifier: Modifier = Modifier,
    currentStep: NewRecipeScreenStep,
    draft: RecipeDraft,
    nextButtonEnabled: Boolean,
    onNextClicked: () -> Unit,
    onFinishClicked: () -> Unit,
    onRecipeNameChanged: (String) -> Unit,
    onRecipeIngredientAdded: (String, Float, String?) -> Unit,
    onRecipeIngredientUpdated: (Long, String, Float, String?) -> Unit,
    onRecipeIngredientRemoved: (Long) -> Unit,
    onRecipeInstructionAdded: (String) -> Unit,
    onRecipeInstructionUpdated: (Long, String) -> Unit,
    onRecipeInstructionRemoved: (Long) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.header_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
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
                    text = stringResource(currentStep.titleResourceId)
                )

                when (currentStep) {
                    NewRecipeScreenStep.NAME -> {
                        NewRecipeNameStep(
                            modifier = modifier,
                            recipeName = draft.name,
                            onRecipeNameChanged = { onRecipeNameChanged(it) }
                        )
                    }

                    NewRecipeScreenStep.INGREDIENTS -> {
                        NewRecipeIngredientsStep(
                            modifier = modifier,
                            ingredients = draft.ingredients,
                            onIngredientAdded = { name, quantity, measurementUnit ->
                                onRecipeIngredientAdded(name, quantity, measurementUnit)
                            },
                            onIngredientUpdated = { key, name, quantity, measurementUnit ->
                                onRecipeIngredientUpdated(key, name, quantity, measurementUnit)
                            },
                            onIngredientRemoved = { onRecipeIngredientRemoved(it) }
                        )
                    }

                    NewRecipeScreenStep.INSTRUCTIONS -> {
                        NewRecipeInstructionsStep(
                            modifier = modifier,
                            instructions = draft.instructions,
                            onInstructionAdded = { onRecipeInstructionAdded(it) },
                            onInstructionUpdated = { key, text ->
                                onRecipeInstructionUpdated(key, text)
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
