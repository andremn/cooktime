package com.nicoapps.cooktime.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.model.Recipe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZonedDateTime

@RunWith(AndroidJUnit4::class)
class LocalRecipeRepositoryTest {

    private lateinit var database: CookTimeDatabase
    private lateinit var repository: LocalRecipeRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            CookTimeDatabase::class.java
        ).build()
        repository = LocalRecipeRepository(database)
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun updatingRecipe_keepsOriginalCreatedAt() = runBlocking {
        val createdAt = ZonedDateTime.now().minusYears(1).withNano(0)
        repository.save(recipe(createdAt = createdAt))
        val saved = repository.getAll().first().single()

        repository.save(
            saved.copy(
                name = "Bolo de cenoura",
                createdAt = ZonedDateTime.now(),
                ingredients = listOf(ingredient()),
                instructions = listOf(Instruction(text = "Misturar"))
            )
        )

        val updated = repository.findById(saved.id).first()
        assertEquals("Bolo de cenoura", updated.name)
        assertEquals(createdAt.toEpochSecond(), updated.createdAt.toEpochSecond())
    }

    @Test
    fun updatingSoftDeletedRecipe_doesNotRestoreIt() = runBlocking {
        repository.save(recipe())
        val saved = repository.getAll().first().single()
        repository.deleteById(saved.id)

        repository.save(saved.copy(name = "Edited while deleted"))

        assertTrue(repository.getAll().first().isEmpty())
    }

    private fun recipe(createdAt: ZonedDateTime = ZonedDateTime.now()) = Recipe(
        name = "Bolo",
        ingredients = listOf(ingredient()),
        instructions = listOf(Instruction(text = "Assar")),
        isStarred = false,
        createdAt = createdAt
    )

    private fun ingredient() = Ingredient(name = "Farinha", quantity = 2f, measurementUnit = "xícaras")
}
