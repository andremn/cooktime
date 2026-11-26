package com.nicoapps.cooktime.data

import com.nicoapps.cooktime.model.RecipeExecution

interface RecipeExecutionRepository {
    suspend fun save(recipeExecution: RecipeExecution)
}
