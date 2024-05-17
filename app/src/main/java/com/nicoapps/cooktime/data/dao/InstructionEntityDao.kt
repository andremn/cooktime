package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nicoapps.cooktime.data.entity.InstructionEntity

@Dao
abstract class InstructionEntityDao {
    @Upsert
    abstract fun upsertAll(instructions: List<InstructionEntity>)

    @Delete
    abstract fun deleteAll(instructions: List<InstructionEntity>)

    @Query("SELECT * FROM instructions WHERE recipeId = :recipeId")
    abstract fun getAllByRecipeId(recipeId: Long): List<InstructionEntity>
}