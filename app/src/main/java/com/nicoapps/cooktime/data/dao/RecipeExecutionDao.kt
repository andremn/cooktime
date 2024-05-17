package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.nicoapps.cooktime.data.entity.RecipeExecutionEntity

@Dao
interface RecipeExecutionDao {
    @Insert
    fun save(recipeExecutionEntity: RecipeExecutionEntity)
}