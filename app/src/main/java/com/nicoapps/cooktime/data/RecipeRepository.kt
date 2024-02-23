package com.nicoapps.cooktime.data

import com.nicoapps.cooktime.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun save(recipe: Recipe)

    suspend fun updateIsStarred(id: Int, isStarred: Boolean)

    fun findById(id: Int): Flow<Recipe?>

    fun getAll(): Flow<List<Recipe>>
}