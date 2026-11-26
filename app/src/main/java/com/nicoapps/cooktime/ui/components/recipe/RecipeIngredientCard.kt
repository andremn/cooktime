package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.utils.FloatUtils.formatQuantity
import com.nicoapps.cooktime.utils.FloatUtils.isSingularQuantity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeIngredientCard(
    modifier: Modifier = Modifier,
    showRemoveIcon: Boolean,
    name: String,
    quantity: Float,
    measurementUnit: String?,
    onClick: () -> Unit,
    onRemoveClick: (() -> Unit)?
) {
    Card(
        onClick = onClick
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            if (showRemoveIcon) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    onClick = {
                        onRemoveClick?.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(
                            R.string.cd_remove_ingredient,
                            name
                        )
                    )
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = quantity.formatQuantity(),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(
                        modifier = Modifier.padding(1.dp)
                    )

                    Text(
                        text = unitLabel(quantity, measurementUnit),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/** The ingredient's unit, or "unit"/"units" (localized, by plural rules) when it has none. */
@Composable
fun Ingredient.unitLabel(): String = unitLabel(quantity, measurementUnit)

/** The unit, or "unit"/"units" (localized, by plural rules) when there is none. */
@Composable
fun unitLabel(quantity: Float, measurementUnit: String?): String =
    measurementUnit.orEmpty().lowercase().ifEmpty {
        stringResource(
            id = if (quantity.isSingularQuantity()) {
                R.string.new_recipe_new_ingredient_unit_one
            } else {
                R.string.new_recipe_new_ingredient_unit_other
            }
        )
    }
