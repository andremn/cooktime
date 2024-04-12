package com.nicoapps.cooktime.data

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.nicoapps.cooktime.data.dao.IngredientEntityDao
import com.nicoapps.cooktime.data.dao.InstructionEntityDao
import com.nicoapps.cooktime.data.dao.RecipeEntityDao
import com.nicoapps.cooktime.data.dao.RecipeExecutionDao
import com.nicoapps.cooktime.data.entity.IngredientEntity
import com.nicoapps.cooktime.data.entity.InstructionEntity
import com.nicoapps.cooktime.data.entity.RecipeEntity
import com.nicoapps.cooktime.data.entity.RecipeExecutionEntity

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        InstructionEntity::class,
        RecipeExecutionEntity::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
abstract class CookTimeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeEntityDao
    abstract fun ingredientDao(): IngredientEntityDao
    abstract fun instructionDao(): InstructionEntityDao
    abstract fun recipeExecutionDao(): RecipeExecutionDao
}