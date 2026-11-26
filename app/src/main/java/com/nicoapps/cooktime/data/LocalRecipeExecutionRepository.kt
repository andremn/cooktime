package com.nicoapps.cooktime.data

import com.nicoapps.cooktime.mapper.RecipeExecutionMapper.toEntity
import com.nicoapps.cooktime.model.RecipeExecution
import javax.inject.Inject

class LocalRecipeExecutionRepository @Inject constructor(
    private val database: CookTimeDatabase
) : RecipeExecutionRepository {
    override suspend fun save(recipeExecution: RecipeExecution) {
        database.recipeExecutionDao().save(recipeExecution.toEntity())
    }
}
