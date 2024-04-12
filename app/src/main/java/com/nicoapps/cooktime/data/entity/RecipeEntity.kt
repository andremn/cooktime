package com.nicoapps.cooktime.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity("recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val isStarred: Boolean,
    val isDeleted: Boolean = false,
    val createdAt: Long = Instant.now().epochSecond,
    val lastUpdatedAt: Long? = null
)