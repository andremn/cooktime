package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nicoapps.cooktime.data.entity.IngredientEntity

@Dao
abstract class IngredientEntityDao {
    @Upsert
    abstract suspend fun upsertAll(ingredients: List<IngredientEntity>)

    @Delete
    abstract suspend fun deleteAll(ingredients: List<IngredientEntity>)

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    abstract suspend fun getAllByRecipeId(recipeId: Long): List<IngredientEntity>
}