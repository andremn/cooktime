package com.nicoapps.cooktime.data

import com.nicoapps.cooktime.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun save(recipe: Recipe)

    suspend fun deleteById(id: Long)

    suspend fun undeleteById(id: Long)

    suspend fun updateIsStarred(id: Long, isStarred: Boolean)

    fun findById(id: Long): Flow<Recipe?>

    fun getAll(): Flow<List<Recipe>>
}