package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nicoapps.cooktime.data.entity.InstructionEntity

@Dao
abstract class InstructionEntityDao {
    @Upsert
    abstract suspend fun upsertAll(instructions: List<InstructionEntity>)

    @Delete
    abstract suspend fun deleteAll(instructions: List<InstructionEntity>)

    @Query("SELECT * FROM instructions WHERE recipeId = :recipeId")
    abstract suspend fun getAllByRecipeId(recipeId: Long): List<InstructionEntity>
}