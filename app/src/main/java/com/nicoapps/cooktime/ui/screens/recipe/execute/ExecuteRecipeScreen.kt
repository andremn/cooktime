package com.nicoapps.cooktime.ui.screens.recipe.execute

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.components.AnimatedLineThroughText
import com.nicoapps.cooktime.ui.components.AppTitleTopBar
import com.nicoapps.cooktime.ui.components.recipe.unitLabel
import com.nicoapps.cooktime.ui.screens.recipe.execute.appbar.ExecuteRecipeBottomAppBarActions
import com.nicoapps.cooktime.ui.screens.recipe.execute.appbar.ExecuteRecipeBottomAppBarFab
import com.nicoapps.cooktime.ui.screens.recipe.execute.dialogs.SaveExecutionDialog
import com.nicoapps.cooktime.utils.FloatUtils.formatQuantity

@Composable
fun ExecuteRecipeScreen(
    modifier: Modifier = Modifier,
    viewModel: ExecuteRecipeViewModel = hiltViewModel(),
    appNavigationActions: AppNavigationActions
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    val onBackPressedDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher

    BackHandler {
        viewModel.onBackPressed()
    }

    LaunchedEffect(screenState.isExecutionSaved) {
        if (screenState.isExecutionSaved) {
            appNavigationActions.navigateBack()
        }
    }

    if (screenState.isSaveExecutionDialogOpen) {
        SaveExecutionDialog(
            onDismissRequest = viewModel::dismissSaveExecutionDialog,
            onConfirmed = viewModel::onSaveExecutionDialogConfirmed,
            onRejected = appNavigationActions::navigateBack
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTitleTopBar(
                title = {
                    Text(text = screenState.recipeName)
                },
                onNavigationIconClick = { onBackPressedDispatcher.onBackPressed() }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    ExecuteRecipeBottomAppBarActions(
                        onResetClick = viewModel::onReset,
                        onAlarmClick = { }
                    )
                },
                floatingActionButton = {
                    ExecuteRecipeBottomAppBarFab(
                        onClick = viewModel::onSaveExecutionDialogConfirmed
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item(key = "ingredientsSection") {
                Section(R.string.view_recipe_ingredients_tab)
            }

            itemsIndexed(screenState.ingredients) { index, ingredientState ->
                Row(
                    modifier = Modifier.toggleable(
                        value = ingredientState.isChecked,
                        role = Role.Checkbox,
                        onValueChange = { viewModel.onIngredientCheckChanged(index, it) }
                    )
                ) {
                    Checkbox(
                        checked = ingredientState.isChecked,
                        onCheckedChange = null
                    )

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        MainText(
                            text = ingredientState.ingredient.name,
                            isChecked = ingredientState.isChecked,
                            supportingText = ingredientState.ingredient.buildAnnotatedString()
                        )
                    }
                }
            }

            item(key = "instructionsSection") {
                Section(R.string.view_recipe_instructions_tab, spacerBeforeText = true)
            }

            itemsIndexed(screenState.instructions) { index, instructionState ->
                Row(
                    modifier = Modifier.toggleable(
                        value = instructionState.isChecked,
                        role = Role.Checkbox,
                        onValueChange = { viewModel.onInstructionCheckChanged(index, it) }
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Checkbox(
                        checked = instructionState.isChecked,
                        onCheckedChange = null
                    )

                    MainText(instructionState.instruction.text, instructionState.isChecked)
                }
            }
        }
    }
}


@Composable
private fun MainText(text: String, isChecked: Boolean, supportingText: AnnotatedString? = null) {
    val textStyle = if (isChecked) MaterialTheme.typography.titleMedium.copy(
        color = TextFieldDefaults.colors().disabledTextColor
    ) else MaterialTheme.typography.titleMedium

    AnimatedLineThroughText(
        text = buildAnnotatedString { append(text) },
        textAlign = TextAlign.Justify,
        style = textStyle,
        isLinedThrough = isChecked
    )

    supportingText?.let {
        val supportingTextStyle = if (isChecked) MaterialTheme.typography.bodyLarge.copy(
            color = TextFieldDefaults.colors().disabledTextColor
        ) else MaterialTheme.typography.bodyLarge

        AnimatedLineThroughText(
            text = supportingText,
            style = supportingTextStyle,
            isLinedThrough = isChecked
        )
    }
}

@Composable
private fun Section(@StringRes textResourceId: Int, spacerBeforeText: Boolean = false) {
    if (spacerBeforeText) {
        Spacer(modifier = Modifier.padding(15.dp))
    }

    Text(
        text = stringResource(id = textResourceId),
        style = MaterialTheme.typography.headlineSmall
    )

    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
private fun Ingredient.buildAnnotatedString() = buildAnnotatedString {
    append(quantity.formatQuantity())
    append(" ")
    append(unitLabel())
}
