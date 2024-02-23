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
            val recipeId = database.recipeDao().insert(recipe.toEntity())

            database.ingredientDao().insertAll(
                recipe.ingredients.map { it.toEntity(recipeId.toInt()) }
            )

            database.instructionDao().insertAll(
                recipe.instructions.map { it.toEntity(recipeId.toInt()) }
            )
        }
    }

    override suspend fun updateIsStarred(id: Int, isStarred: Boolean) =
        database.recipeDao().updateIsStarred(id, isStarred)

    override fun findById(id: Int) =
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