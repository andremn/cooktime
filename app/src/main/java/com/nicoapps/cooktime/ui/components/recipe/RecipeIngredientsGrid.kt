package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.screens.recipe.draft.DraftIngredient
import com.nicoapps.cooktime.utils.FloatUtils.formatQuantity

@Composable
fun RecipeIngredientsGrid(
    modifier: Modifier = Modifier,
    ingredients: List<DraftIngredient>,
    mode: RecipeIngredientsGridMode,
    onIngredientAdded: (name: String, quantity: Float, measurementUnit: String?) -> Unit,
    onIngredientUpdated: (
        key: Long,
        name: String,
        quantity: Float,
        measurementUnit: String?
    ) -> Unit,
    onIngredientRemoved: (key: Long) -> Unit
) {
    var isNewIngredientDialogOpen by rememberSaveable { mutableStateOf(false) }
    var editingIngredientKey by rememberSaveable { mutableStateOf<Long?>(null) }
    var name by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var measurementUnit by rememberSaveable { mutableStateOf("") }

    fun closeDialog() {
        isNewIngredientDialogOpen = false
        editingIngredientKey = null
        name = ""
        quantity = ""
        measurementUnit = ""
    }

    Column(
        modifier = modifier,
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
                // Takes the height left after the add button, so a long list scrolls inside
                // the screen.
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .animateContentSize(),
                columns = StaggeredGridCells.Adaptive(150.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp,
            ) {
                items(
                    items = ingredients,
                    key = { ingredient -> ingredient.key }
                ) { ingredient ->
                    RecipeIngredientCard(
                        modifier = modifier,
                        showRemoveIcon = mode.isEditing(),
                        name = ingredient.name,
                        quantity = ingredient.quantity,
                        measurementUnit = ingredient.measurementUnit,
                        onClick = {
                            if (mode.isEditing()) {
                                editingIngredientKey = ingredient.key
                                name = ingredient.name
                                quantity = ingredient.quantity.formatQuantity()
                                measurementUnit = ingredient.measurementUnit.orEmpty()
                                isNewIngredientDialogOpen = true
                            }
                        },
                        onRemoveClick = {
                            if (mode.isEditing()) {
                                onIngredientRemoved(ingredient.key)
                            }
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacer_padding)),
            visible = mode.isEditing()
        ) {
            ElevatedButton(
                onClick = { isNewIngredientDialogOpen = true }
            ) {
                Text(
                    text = stringResource(id = R.string.new_recipe_add_ingredient_button)
                )
            }
        }

        NewIngredientDialog(
            isOpen = isNewIngredientDialogOpen,
            name = name,
            quantity = quantity,
            measurementUnit = measurementUnit,
            onNameChanged = { name = it },
            onQuantityChanged = { quantity = it },
            onMeasurementUnitChanged = { measurementUnit = it },
            onConfirmed = { confirmedName, confirmedQuantity, confirmedUnit ->
                val editedIngredientKey = editingIngredientKey

                if (editedIngredientKey != null) {
                    onIngredientUpdated(
                        editedIngredientKey,
                        confirmedName,
                        confirmedQuantity,
                        confirmedUnit
                    )
                } else {
                    onIngredientAdded(
                        confirmedName,
                        confirmedQuantity,
                        confirmedUnit
                    )
                }

                closeDialog()
            },
            onDismiss = { closeDialog() }
        )
    }
}
