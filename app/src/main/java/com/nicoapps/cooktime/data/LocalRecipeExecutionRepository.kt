package com.nicoapps.cooktime.data

import com.nicoapps.cooktime.data.entity.RecipeExecutionEntity
import javax.inject.Inject

class LocalRecipeExecutionRepository @Inject constructor(
    private val database: CookTimeDatabase
) : RecipeExecutionRepository {
    override suspend fun save(recipeExecution: RecipeExecutionEntity) {
        database.recipeExecutionDao().save(recipeExecution)
    }
}