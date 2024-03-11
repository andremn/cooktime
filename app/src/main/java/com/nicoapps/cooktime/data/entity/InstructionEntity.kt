package com.nicoapps.cooktime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity("instructions")
data class InstructionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recipeId: Long = 0,
    val text: String,
    val createdAt: Long = Instant.now().epochSecond,
    val lastUpdatedAt: Long? = null
)
