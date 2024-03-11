package com.nicoapps.cooktime.data

import androidx.room.withTransaction
import com.nicoapps.cooktime.mapper.IngredientMapper.toEntity
import com.nicoapps.cooktime.mapper.InstructionMapper.toEntity
import com.nicoapps.cooktime.mapper.RecipeMapper.toEntity
import com.nicoapps.cooktime.mapper.RecipeMapper.toModel
import com.nicoapps.cooktime.model.Recipe
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRecipeRepository @Inject constructor(
    private val database: CookTimeDatabase
) : RecipeRepository {

    override suspend fun save(recipe: Recipe) {
        database.withTransaction {
            var recipeId = database.recipeDao().upsert(recipe.toEntity())

            if (recipe.id > 0) {
                recipeId = recipe.id
            }

            database.ingredientDao().upsertAll(
                recipe.ingredients.map { it.toEntity(recipeId) }
            )

            database.instructionDao().upsertAll(
                recipe.instructions.map { it.toEntity(recipeId) }
            )
        }
    }

    override suspend fun deleteById(id: Long) {
        database.recipeDao().deleteById(id)
    }

    override suspend fun updateIsStarred(id: Long, isStarred: Boolean) =
        database.recipeDao().updateIsStarred(id, isStarred)

    override fun findById(id: Long) =
        database.recipeDao().getWithDetailsById(id)
            .filterNotNull()
            .map { it.toModel() }

    override fun getAll() =
        database.recipeDao().getAll().map { recipes ->
            recipes.map { it.toModel() }
        }.also {
            println("getAll() called!")
        }
}