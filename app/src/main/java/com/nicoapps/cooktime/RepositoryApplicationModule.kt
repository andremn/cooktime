package com.nicoapps.cooktime

import com.nicoapps.cooktime.data.LocalRecipeExecutionRepository
import com.nicoapps.cooktime.data.LocalRecipeRepository
import com.nicoapps.cooktime.data.RecipeExecutionRepository
import com.nicoapps.cooktime.data.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryApplicationModule {

    @Binds
    @ViewModelScoped
    @LocalRepository
    abstract fun bindLocalRecipeRepository(
        localRepository: LocalRecipeRepository
    ): RecipeRepository

    @Binds
    @ViewModelScoped
    @LocalRepository
    abstract fun bindLocalRecipeExecutionRepository(
        localRepository: LocalRecipeExecutionRepository
    ): RecipeExecutionRepository
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalRepository