package com.nicoapps.cooktime.mapper

import com.nicoapps.cooktime.data.entity.IngredientEntity
import com.nicoapps.cooktime.model.Ingredient
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object IngredientMapper {
    fun Ingredient.toEntity(recipeId: Int = 0) =
        IngredientEntity(
            id = id,
            recipeId = recipeId,
            quantity = quantity,
            measurementUnit = measurementUnit,
            name = name,
            createdAt = createdAt.toEpochSecond(),
            lastUpdatedAt = lastUpdatedAt?.toEpochSecond()
        )

    fun IngredientEntity.toModel() =
        Ingredient(
            id = id,
            quantity = quantity,
            measurementUnit = measurementUnit,
            name = name,
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