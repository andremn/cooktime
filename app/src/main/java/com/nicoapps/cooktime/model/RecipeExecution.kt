package com.nicoapps.cooktime.model

import java.time.ZonedDateTime

data class RecipeExecution(
    val id: Long = 0,
    val recipeId: Long,
    val createdAt: ZonedDateTime = ZonedDateTime.now()
)
