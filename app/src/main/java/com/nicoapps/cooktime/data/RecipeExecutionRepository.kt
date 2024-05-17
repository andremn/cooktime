package com.nicoapps.cooktime.data

import com.nicoapps.cooktime.data.entity.RecipeExecutionEntity

interface RecipeExecutionRepository {
    suspend fun save(recipeExecution: RecipeExecutionEntity)
}