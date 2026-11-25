package com.nicoapps.cooktime.data.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Version 2 already has the `isDeleted` column (added by the 1→2 auto-migration).
 * Version 3 only dropped its `DEFAULT false`, which SQLite can't change without
 * recreating the table and Room doesn't validate for columns whose entity has no
 * default, so there's nothing to do here.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) = Unit
}

val ALL_MIGRATIONS = arrayOf(MIGRATION_2_3)
