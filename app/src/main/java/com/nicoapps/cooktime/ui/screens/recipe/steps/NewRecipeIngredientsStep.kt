package com.nicoapps.cooktime.ui.screens.recipe.steps

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.ui.components.NewIngredientDialog
import com.nicoapps.cooktime.utils.FloatUtils.formatQuantity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeIngredientsStep(
    modifier: Modifier = Modifier,
    ingredients: List<Ingredient>,
    onCurrentTitleChanged: (String) -> Unit,
    onIngredientAdded: (Ingredient) -> Unit,
    onIngredientUpdated: (Ingredient) -> Unit,
    onIngredientRemoved: (Ingredient) -> Unit,
) {
    val context = LocalContext.current

    var isNewIngredientDialogOpen by remember { mutableStateOf(false) }
    var isEditingIngredient by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var measurementUnit by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        onCurrentTitleChanged(
            context.resources.getString(R.string.new_recipe_ingredients_step_title)
        )
    }

    fun clearFields() {
        name = ""
        quantity = ""
        measurementUnit = ""
    }

    fun showQuantityDialog() {
        isNewIngredientDialogOpen = true
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (ingredients.isEmpty()) {
            Text(
                text = stringResource(id = R.string.new_recipe_add_ingredient_empty),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        } else {
            LazyVerticalStaggeredGrid(
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .animateContentSize(),
                columns = StaggeredGridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp,
            ) {
                items(ingredients) { ingredient ->
                    key(ingredient.name) {
                        Card(
                            onClick = {
                                isEditingIngredient = true
                                name = ingredient.name
                                quantity = ingredient.quantity.formatQuantity()
                                measurementUnit = ingredient.measurementUnit.orEmpty()
                                isNewIngredientDialogOpen = true
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                IconButton(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd),
                                    onClick = {
                                        onIngredientRemoved(ingredient)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = null
                                    )
                                }

                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                        .align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = ingredient.name,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = ingredient.quantity.formatQuantity(),
                                            style = MaterialTheme.typography.bodyLarge
                                        )

                                        Spacer(
                                            modifier = Modifier.padding(1.dp)
                                        )

                                        Text(
                                            text = ingredient.measurementUnit.orEmpty().ifEmpty {
                                                stringResource(
                                                    id = R.string.new_recipe_new_ingredient_unitary
                                                )
                                            }.lowercase(),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.spacer_padding))
        )

        ElevatedButton(
            onClick = { showQuantityDialog() }
        ) {
            Text(
                text = "+ ${stringResource(id = R.string.new_recipe_add_ingredient)}"
            )
        }

        NewIngredientDialog(
            isOpen = isNewIngredientDialogOpen,
            name = name,
            quantity = quantity,
            measurementUnit = measurementUnit,
            onNameChanged = { name = it },
            onQuantityChanged = { quantity = it },
            onMeasurementUnitChanged = { measurementUnit = it },
            onCloseRequest = { confirmed ->
                isNewIngredientDialogOpen = false
                isEditingIngredient = false

                if (confirmed) {
                    val addedOrUpdatedIngredient = Ingredient(
                        name = name,
                        quantity = quantity.toFloatOrNull() ?: 0f,
                        measurementUnit = measurementUnit
                    )

                    if (isEditingIngredient) {
                        onIngredientUpdated(addedOrUpdatedIngredient)
                    } else {
                        onIngredientAdded(addedOrUpdatedIngredient)
                    }

                    clearFields()
                }
            }
        )
    }
}