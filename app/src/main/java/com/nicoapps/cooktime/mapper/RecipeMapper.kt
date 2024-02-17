package com.nicoapps.cooktime.mapper

import com.nicoapps.cooktime.data.entity.IngredientEntity
import com.nicoapps.cooktime.data.entity.InstructionEntity
import com.nicoapps.cooktime.data.entity.RecipeEntity
import com.nicoapps.cooktime.data.entity.RecipeWithDetailsEntity
import com.nicoapps.cooktime.mapper.IngredientMapper.toModel
import com.nicoapps.cooktime.mapper.InstructionMapper.toModel
import com.nicoapps.cooktime.model.Recipe
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object RecipeMapper {
    fun Recipe.toEntity() =
        RecipeEntity(
            id = id,
            name = name,
            createdAt = createdAt.toEpochSecond(),
            lastUpdatedAt = lastUpdatedAt?.toEpochSecond()
        )

    fun RecipeWithDetailsEntity.toModel() =
        Recipe(
            id = recipe.id,
            name = recipe.name,
            ingredients = ingredients.map { it.toModel() },
            instructions = instructions.map { it.toModel() },
            createdAt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(recipe.createdAt),
                ZoneId.systemDefault()
            ),
            lastUpdatedAt = recipe.lastUpdatedAt?.let {
                ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(it),
                    ZoneId.systemDefault()
                )
            }
        )

    fun RecipeEntity.toModel(
        ingredients: List<IngredientEntity> = emptyList(),
        instructions: List<InstructionEntity> = emptyList()
    ) =
        Recipe(
            id = id,
            name = name,
            ingredients = ingredients.map { it.toModel() },
            instructions = instructions.map { it.toModel() },
            createdAt = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(createdAt),
                ZoneId.systemDefault()
            ),
            lastUpdatedAt = lastUpdatedAt?.let {
                ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(it),
                    ZoneId.systemDefault()
                )
            }
        )
}