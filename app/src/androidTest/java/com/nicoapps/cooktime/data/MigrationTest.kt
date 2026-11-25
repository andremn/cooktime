package com.nicoapps.cooktime.data

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nicoapps.cooktime.data.migrations.ALL_MIGRATIONS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        CookTimeDatabase::class.java
    )

    @Test
    fun migrateFrom1To3_keepsRecipesAndAddsIsDeleted() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                "INSERT INTO recipes (id, name, isStarred, createdAt) " +
                    "VALUES (1, 'Bolo', 1, $CREATED_AT)"
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 3, true, *ALL_MIGRATIONS)

        db.query("SELECT name, isStarred, isDeleted, createdAt FROM recipes WHERE id = 1").use {
            assertTrue(it.moveToFirst())
            assertEquals("Bolo", it.getString(0))
            assertEquals(1, it.getInt(1))
            assertEquals(0, it.getInt(2))
            assertEquals(CREATED_AT, it.getLong(3))
        }
    }

    @Test
    fun migrateFrom2To3_keepsSoftDeletedFlag() {
        helper.createDatabase(TEST_DB, 2).apply {
            execSQL(
                "INSERT INTO recipes (id, name, isStarred, isDeleted, createdAt) " +
                    "VALUES (1, 'Bolo', 0, 1, $CREATED_AT)"
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 3, true, *ALL_MIGRATIONS)

        db.query("SELECT isDeleted FROM recipes WHERE id = 1").use {
            assertTrue(it.moveToFirst())
            assertEquals(1, it.getInt(0))
        }
    }

    private companion object {
        const val TEST_DB = "migration-test"
        const val CREATED_AT = 1_700_000_000L
    }
}
