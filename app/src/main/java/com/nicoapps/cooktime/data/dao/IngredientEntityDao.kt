package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.nicoapps.cooktime.data.entity.IngredientEntity

@Dao
abstract class IngredientEntityDao {
    @Upsert
    abstract fun upsertAll(ingredients: List<IngredientEntity>)
}