package com.nicoapps.cooktime.data

import android.content.Context
import com.nicoapps.cooktime.LocalRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

object RecipeRepositoryProvider {
    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface RecipeRepositoryProviderEntryPoint {
        @LocalRepository
        fun getRecipeRepository(): RecipeRepository
    }

    fun getRecipeRepository(context: Context) : RecipeRepository {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context,
            RecipeRepositoryProviderEntryPoint::class.java
        )

        return hiltEntryPoint.getRecipeRepository()
    }
}