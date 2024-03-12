package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.nicoapps.cooktime.data.entity.RecipeEntity
import com.nicoapps.cooktime.data.entity.RecipeWithDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeEntityDao {

    @Upsert
    suspend fun upsert(recipe: RecipeEntity): Long

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE recipes SET isStarred = :isStarred WHERE id = :id")
    suspend fun updateIsStarred(id: Long, isStarred: Boolean)

    @Query("SELECT * FROM recipes")
    fun getAll(): Flow<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getWithDetailsById(id: Long): Flow<RecipeWithDetailsEntity?>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: Long): Flow<RecipeEntity?>
}