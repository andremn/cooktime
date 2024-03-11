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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeIngredientCard(
    modifier: Modifier = Modifier,
    ingredient: Ingredient,
    onRemoveClick: (Ingredient) -> Unit,
    onClick: (Ingredient) -> Unit
) {
    Card(
        onClick = { onClick(ingredient) }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = {
                    onRemoveClick(ingredient)
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