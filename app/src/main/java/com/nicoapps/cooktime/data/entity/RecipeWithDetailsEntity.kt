package com.nicoapps.cooktime.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RecipeWithDetailsEntity(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<IngredientEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val instructions: List<InstructionEntity>
)
