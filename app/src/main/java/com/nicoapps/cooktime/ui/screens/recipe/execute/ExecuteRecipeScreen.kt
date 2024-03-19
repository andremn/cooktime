package com.nicoapps.cooktime.ui.screens.recipe.execute

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavGraphTopBarContentType
import com.nicoapps.cooktime.ui.AppNavGraphTopBarState
import com.nicoapps.cooktime.ui.AppNavigationActions

@Composable
fun ExecuteRecipeScreen(
    modifier: Modifier = Modifier,
    viewModel: ExecuteRecipeViewModel = hiltViewModel(),
    appNavigationActions: AppNavigationActions,
    onComposing: (AppNavGraphState) -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        onComposing(
            AppNavGraphState(
                topBar = AppNavGraphTopBarState(
                    contentType = AppNavGraphTopBarContentType.TITLE_ONLY,
                    title = { Text(text = screenState.recipeName) },
                    showActions = true,
                    actions = {
                        IconButton(
                            onClick = {
                                appNavigationActions.navigateBack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            )
        )
    }

    LazyColumn(
        modifier = modifier
    ) {
        item(key = "ingredientsSection") {
            Text(
                text = stringResource(id = R.string.view_recipe_ingredients_tab)
            )

            Spacer(modifier = Modifier.padding(5.dp))
        }

        itemsIndexed(screenState.ingredients) { index, ingredientState ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Checkbox(
                    checked = ingredientState.isChecked,
                    onCheckedChange = {
                        viewModel.onIngredientCheckChanged(index, it)
                    }
                )

                Text(text = ingredientState.ingredient.name)
            }
        }

        item(key = "instructionsSection") {
            Spacer(modifier = Modifier.padding(15.dp))

            Text(
                text = stringResource(id = R.string.view_recipe_instructions_tab)
            )

            Spacer(modifier = Modifier.padding(5.dp))
        }

        itemsIndexed(screenState.instructions) { index, instructionState ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Checkbox(
                    checked = instructionState.isChecked,
                    onCheckedChange = {
                        viewModel.onInstructionCheckChanged(index, it)
                    })

                Text(text = instructionState.instruction.text)
            }
        }
    }
}