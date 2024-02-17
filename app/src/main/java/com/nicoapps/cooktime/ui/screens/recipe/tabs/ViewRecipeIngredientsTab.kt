package com.nicoapps.cooktime.ui.screens.recipe.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.utils.FloatUtils.formatQuantity

@Composable
fun ViewRecipeIngredientsTab(
    modifier: Modifier = Modifier,
    recipeIngredients: List<Ingredient>
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(150.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalItemSpacing = 10.dp,
    ) {
        items(recipeIngredients) { ingredient ->
            key(ingredient.name) {
                Card {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
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