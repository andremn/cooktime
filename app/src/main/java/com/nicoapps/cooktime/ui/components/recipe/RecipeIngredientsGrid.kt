package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.utils.FloatUtils.formatQuantity

@Composable
fun RecipeIngredientsGrid(
    modifier: Modifier = Modifier,
    ingredients: List<Ingredient>,
    onIngredientAdded: (Ingredient) -> Unit,
    onIngredientUpdated: (Int, Ingredient) -> Unit,
    onIngredientRemoved: (Ingredient) -> Unit,
) {
    var isNewIngredientDialogOpen by remember { mutableStateOf(false) }
    var editingIngredientIndex by remember { mutableIntStateOf(-1) }
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var measurementUnit by remember { mutableStateOf("") }

    fun clearFields() {
        name = ""
        quantity = ""
        measurementUnit = ""
    }

    fun resetDialogState() {
        isNewIngredientDialogOpen = false
        editingIngredientIndex = -1
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
                itemsIndexed(ingredients) { index, ingredient ->
                    key(ingredient.name) {
                        RecipeIngredientCard(
                            modifier = modifier,
                            ingredient = ingredient,
                            onClick = {
                                editingIngredientIndex = index
                                name = ingredient.name
                                quantity = ingredient.quantity.formatQuantity()
                                measurementUnit = ingredient.measurementUnit.orEmpty()
                                isNewIngredientDialogOpen = true
                            },
                            onRemoveClick = { onIngredientRemoved(it) }
                        )
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
                if (confirmed) {
                    val addedOrUpdatedIngredient = Ingredient(
                        name = name,
                        quantity = quantity.toFloatOrNull() ?: 0f,
                        measurementUnit = measurementUnit
                    )

                    if (editingIngredientIndex >= 0) {
                        onIngredientUpdated(editingIngredientIndex, addedOrUpdatedIngredient)
                    } else {
                        onIngredientAdded(addedOrUpdatedIngredient)
                    }
                }

                clearFields()
                resetDialogState()
            }
        )
    }
}