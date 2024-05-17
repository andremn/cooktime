package com.nicoapps.cooktime

import com.nicoapps.cooktime.data.LocalRecipeExecutionRepository
import com.nicoapps.cooktime.data.LocalRecipeRepository
import com.nicoapps.cooktime.data.RecipeExecutionRepository
import com.nicoapps.cooktime.data.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryApplicationModule {

    @Binds
    @LocalRepository
    abstract fun bindLocalRecipeRepository(
        localRepository: LocalRecipeRepository
    ): RecipeRepository

    @Binds
    @LocalRepository
    abstract fun bindLocalRecipeExecutionRepository(
        localRepository: LocalRecipeExecutionRepository
    ): RecipeExecutionRepository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalRepository