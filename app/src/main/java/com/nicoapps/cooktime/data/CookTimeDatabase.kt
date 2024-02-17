package com.nicoapps.cooktime.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nicoapps.cooktime.data.dao.IngredientEntityDao
import com.nicoapps.cooktime.data.dao.InstructionEntityDao
import com.nicoapps.cooktime.data.dao.RecipeEntityDao
import com.nicoapps.cooktime.data.entity.IngredientEntity
import com.nicoapps.cooktime.data.entity.InstructionEntity
import com.nicoapps.cooktime.data.entity.RecipeEntity

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        InstructionEntity::class
    ],
    version = 1
)
abstract class CookTimeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeEntityDao
    abstract fun ingredientDao(): IngredientEntityDao
    abstract fun instructionDao(): InstructionEntityDao
}