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
    suspend fun insert(recipe: RecipeEntity): Long

    /**
     * Updates only the user-editable columns, so `createdAt` and `isDeleted` are never
     * overwritten by an edit.
     */
    @Query(
        "UPDATE recipes SET name = :name, isStarred = :isStarred, " +
            "lastUpdatedAt = :lastUpdatedAt WHERE id = :id"
    )
    suspend fun updateDetails(id: Long, name: String, isStarred: Boolean, lastUpdatedAt: Long)

    @Query("UPDATE recipes SET isDeleted = true WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE recipes SET isDeleted = false WHERE id = :id")
    suspend fun undeleteById(id: Long)

    @Query("UPDATE recipes SET isStarred = :isStarred WHERE id = :id")
    suspend fun updateIsStarred(id: Long, isStarred: Boolean)

    @Query("SELECT * FROM recipes WHERE isDeleted = false")
    fun getAll(): Flow<List<RecipeEntity>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id AND isDeleted = false")
    fun getWithDetailsById(id: Long): Flow<RecipeWithDetailsEntity?>

    @Query("SELECT * FROM recipes WHERE id = :id AND isDeleted = false")
    fun getById(id: Long): Flow<RecipeEntity?>
}