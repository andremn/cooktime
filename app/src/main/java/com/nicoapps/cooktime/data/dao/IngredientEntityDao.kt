package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nicoapps.cooktime.data.entity.IngredientEntity

@Dao
abstract class IngredientEntityDao {
    @Upsert
    abstract fun upsertAll(ingredients: List<IngredientEntity>)

    @Delete
    abstract fun deleteAll(ingredients: List<IngredientEntity>)

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    abstract fun getAllByRecipeId(recipeId: Long): List<IngredientEntity>
}