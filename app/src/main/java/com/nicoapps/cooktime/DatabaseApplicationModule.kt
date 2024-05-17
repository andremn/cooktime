package com.nicoapps.cooktime

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nicoapps.cooktime.data.CookTimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseApplicationModule {

    private val migration2to3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE recipes ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

    @Singleton
    @Provides
    fun providesCoroutineScope() =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CookTimeDatabase {
        return Room.databaseBuilder(
            appContext,
            CookTimeDatabase::class.java,
            DATA_BASE_NAME
        ).addMigrations(migration2to3).build()
    }

    companion object {
        const val DATA_BASE_NAME = "cook-time"
    }
}