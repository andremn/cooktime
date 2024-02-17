package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.nicoapps.cooktime.data.entity.InstructionEntity

@Dao
abstract class InstructionEntityDao {
    @Insert
    abstract fun insertAll(instructions: List<InstructionEntity>)
}