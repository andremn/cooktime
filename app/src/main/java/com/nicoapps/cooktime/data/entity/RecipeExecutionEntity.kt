package com.nicoapps.cooktime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity("recipe_executions")
data class RecipeExecutionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: Long = 0,
    val createdAt: Long = Instant.now().epochSecond
)
