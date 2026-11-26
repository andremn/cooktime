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
        val saved = repository.findById(repository.getAll().first().single().id).first()

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
        val saved = repository.findById(repository.getAll().first().single().id).first()
        repository.deleteById(saved.id)

        repository.save(saved.copy(name = "Edited while deleted"))

        assertTrue(repository.getAll().first().isEmpty())
    }

    @Test
    fun searchByName_matchesPartOfTheNameIgnoringAsciiCase() = runBlocking {
        repository.save(recipe(name = "Bolo de cenoura"))
        repository.save(recipe(name = "Omelete"))

        val names = repository.searchByName("CENOURA").first().map { it.name }

        assertEquals(listOf("Bolo de cenoura"), names)
    }

    @Test
    fun searchByName_leavesOutDeletedRecipes() = runBlocking {
        repository.save(recipe(name = "Bolo"))
        repository.deleteById(repository.getAll().first().single().id)

        assertTrue(repository.searchByName("Bolo").first().isEmpty())
    }

    @Test
    fun searchByName_treatsWildcardsLiterally() = runBlocking {
        repository.save(recipe(name = "Bolo 100% integral"))
        repository.save(recipe(name = "Bolo integral"))
        repository.save(recipe(name = "Pão_de_queijo"))
        repository.save(recipe(name = "Pão de queijo"))

        assertEquals(
            listOf("Bolo 100% integral"),
            repository.searchByName("100%").first().map { it.name }
        )
        assertEquals(
            listOf("Pão_de_queijo"),
            repository.searchByName("_de_").first().map { it.name }
        )
    }

    private fun recipe(
        name: String = "Bolo",
        createdAt: ZonedDateTime = ZonedDateTime.now()
    ) = Recipe(
        name = name,
        ingredients = listOf(ingredient()),
        instructions = listOf(Instruction(text = "Assar")),
        isStarred = false,
        createdAt = createdAt
    )

    private fun ingredient() = Ingredient(name = "Farinha", quantity = 2f, measurementUnit = "xícaras")
}
