package com.nicoapps.cooktime.data

import com.nicoapps.cooktime.model.Recipe
import com.nicoapps.cooktime.model.RecipeSummary
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    /**
     * Inserts [recipe] when its id is 0, otherwise updates it. On update, only the editable
     * fields and children are written: `createdAt` and the deleted flag are kept as stored,
     * and `lastUpdatedAt` is set by the repository, so callers don't need to provide them.
     */
    suspend fun save(recipe: Recipe)

    suspend fun deleteById(id: Long)

    suspend fun undeleteById(id: Long)

    suspend fun updateIsStarred(id: Long, isStarred: Boolean)

    fun findById(id: Long): Flow<Recipe?>

    /** Lists the recipes that aren't deleted, without their ingredients and instructions. */
    fun getAll(): Flow<List<RecipeSummary>>
}