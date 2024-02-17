package com.nicoapps.cooktime.ui.screens.recipe.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.model.Instruction

@Composable
fun ViewRecipeInstructionsTab(
    modifier: Modifier = Modifier,
    recipeInstructions: List<Instruction>
) {
    LazyColumn(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        itemsIndexed(recipeInstructions) { index, instruction ->
            key(index) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        text = "${index.inc()}. ${instruction.text}"
                    )
                }
            }
        }
    }
}