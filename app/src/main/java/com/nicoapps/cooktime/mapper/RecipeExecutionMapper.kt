package com.nicoapps.cooktime.mapper

import com.nicoapps.cooktime.data.entity.RecipeExecutionEntity
import com.nicoapps.cooktime.model.RecipeExecution

object RecipeExecutionMapper {
    fun RecipeExecution.toEntity() =
        RecipeExecutionEntity(
            id = id,
            recipeId = recipeId,
            createdAt = createdAt.toEpochSecond()
        )
}
