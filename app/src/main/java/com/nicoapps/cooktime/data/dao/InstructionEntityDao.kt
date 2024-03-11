package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.nicoapps.cooktime.data.entity.InstructionEntity

@Dao
abstract class InstructionEntityDao {
    @Upsert
    abstract fun upsertAll(instructions: List<InstructionEntity>)
}