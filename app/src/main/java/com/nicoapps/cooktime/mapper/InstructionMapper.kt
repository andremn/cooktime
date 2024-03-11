package com.nicoapps.cooktime.mapper

import com.nicoapps.cooktime.data.entity.InstructionEntity
import com.nicoapps.cooktime.model.Instruction
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object InstructionMapper {
    fun Instruction.toEntity(recipeId: Long = 0) =
        InstructionEntity(
            id = id,
            recipeId = recipeId,
            text = text,
            createdAt = createdAt.toEpochSecond(),
            lastUpdatedAt = lastUpdatedAt?.toEpochSecond()
        )

    fun InstructionEntity.toModel() =
        Instruction(
            id = id,
            text = text,
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