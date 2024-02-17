package com.nicoapps.cooktime.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.nicoapps.cooktime.data.entity.RecipeEntity
import com.nicoapps.cooktime.data.entity.RecipeWithDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeEntityDao {

    @Insert
    fun insert(recipe: RecipeEntity): Long

    @Query("SELECT * FROM recipes")
    fun getAll(): Flow<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getWithDetailsById(id: Int): Flow<RecipeWithDetailsEntity?>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: Int): Flow<RecipeEntity?>
}