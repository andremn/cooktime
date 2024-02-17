package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.nicoapps.cooktime.data.entity.IngredientEntity

@Dao
abstract class IngredientEntityDao {
    @Insert
    abstract fun insertAll(ingredients: List<IngredientEntity>)
}