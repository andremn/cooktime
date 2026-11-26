package com.nicoapps.cooktime.ui.screens.recipe.draft

import com.nicoapps.cooktime.model.Ingredient
import com.nicoapps.cooktime.model.Instruction
import com.nicoapps.cooktime.model.Recipe
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class RecipeDraftTest {

    @Test
    fun addingDuplicateIngredients_createsDistinctKeys() {
        val draft = RecipeDraft()
            .addIngredient(name = "Milk", quantity = 1f, measurementUnit = "cup")
            .addIngredient(name = "Milk", quantity = 1f, measurementUnit = "cup")

        assertEquals(2, draft.ingredients.size)
        assertNotEquals(draft.ingredients[0].key, draft.ingredients[1].key)
    }

    @Test
    fun updatingOneOfTwoIdenticalIngredients_updatesOnlyThatItem() {
        val draft = RecipeDraft()
            .addIngredient(name = "Milk", quantity = 1f, measurementUnit = "cup")
            .addIngredient(name = "Milk", quantity = 1f, measurementUnit = "cup")
        val keyToUpdate = draft.ingredients[1].key

        val updated = draft.updateIngredient(
            key = keyToUpdate,
            name = "Milk",
            quantity = 2f,
            measurementUnit = "cup"
        )

        assertEquals(1f, updated.ingredients[0].quantity)
        assertEquals(2f, updated.ingredients[1].quantity)
    }

    @Test
    fun removingOneOfTwoIdenticalIngredients_removesOnlyThatItem() {
        val draft = RecipeDraft()
            .addIngredient(name = "Milk", quantity = 1f, measurementUnit = "cup")
            .addIngredient(name = "Milk", quantity = 1f, measurementUnit = "cup")
        val keyToKeep = draft.ingredients[0].key
        val keyToRemove = draft.ingredients[1].key

        val updated = draft.removeIngredient(keyToRemove)

        assertEquals(listOf(keyToKeep), updated.ingredients.map { it.key })
    }

    @Test
    fun addingDuplicateInstructions_createsDistinctKeys() {
        val draft = RecipeDraft()
            .addInstruction("Stir")
            .addInstruction("Stir")

        assertEquals(2, draft.instructions.size)
        assertNotEquals(draft.instructions[0].key, draft.instructions[1].key)
    }

    @Test
    fun updatingOneOfTwoIdenticalInstructions_updatesOnlyThatItem() {
        val draft = RecipeDraft()
            .addInstruction("Stir")
            .addInstruction("Stir")
        val keyToUpdate = draft.instructions[1].key

        val updated = draft.updateInstruction(keyToUpdate, "Stir well")

        assertEquals("Stir", updated.instructions[0].text)
        assertEquals("Stir well", updated.instructions[1].text)
    }

    @Test
    fun removingOneOfTwoIdenticalInstructions_removesOnlyThatItem() {
        val draft = RecipeDraft()
            .addInstruction("Stir")
            .addInstruction("Stir")
        val keyToKeep = draft.instructions[0].key

        val updated = draft.removeInstruction(draft.instructions[1].key)

        assertEquals(listOf(keyToKeep), updated.instructions.map { it.key })
    }

    @Test
    fun operationsWithUnknownKeys_returnTheSameDraft() {
        val draft = RecipeDraft()
            .addIngredient(name = "Milk", quantity = 1f, measurementUnit = "cup")
            .addInstruction("Stir")

        assertSame(
            draft,
            draft.updateIngredient(UNKNOWN_KEY, "Milk", 2f, "cup")
        )
        assertSame(draft, draft.removeIngredient(UNKNOWN_KEY))
        assertSame(draft, draft.updateInstruction(UNKNOWN_KEY, "Mix"))
        assertSame(draft, draft.removeInstruction(UNKNOWN_KEY))
    }

    @Test
    fun validation_checksEachPartSeparately() {
        val empty = RecipeDraft()

        assertFalse(empty.isNameValid)
        assertFalse(empty.areIngredientsValid)
        assertFalse(empty.areInstructionsValid)
        assertFalse(empty.isValid)

        val named = empty.rename("Cake")

        assertTrue(named.isNameValid)
        assertFalse(named.areIngredientsValid)
        assertFalse(named.isValid)

        val withIngredient = named.addIngredient("Flour", 2f, "cups")

        assertTrue(withIngredient.areIngredientsValid)
        assertFalse(withIngredient.areInstructionsValid)
        assertFalse(withIngredient.isValid)

        val complete = withIngredient.addInstruction("Bake")

        assertTrue(complete.areInstructionsValid)
        assertTrue(complete.isValid)
    }

    @Test
    fun updatingAnExistingIngredient_keepsItsIdAndTimestamps() {
        val draft = RecipeDraft.from(recipe())
        val key = draft.ingredients.single().key

        val updated = draft.updateIngredient(
            key = key,
            name = "Sugar",
            quantity = 3f,
            measurementUnit = "g"
        )
        val ingredient = updated.toRecipe().ingredients.single()

        assertEquals(INGREDIENT_ID, ingredient.id)
        assertEquals(INGREDIENT_CREATED_AT.toEpochSecond(), ingredient.createdAt.toEpochSecond())
        assertEquals(
            INGREDIENT_UPDATED_AT.toEpochSecond(),
            ingredient.lastUpdatedAt?.toEpochSecond()
        )
        assertEquals("Sugar", ingredient.name)
    }

    @Test
    fun conversionRoundTrip_preservesItemIdsAndTimestamps() {
        val original = recipe()

        val roundTripped = RecipeDraft.from(original).toRecipe(
            id = original.id,
            isStarred = original.isStarred
        )

        assertEquals(original.id, roundTripped.id)
        assertEquals(original.name, roundTripped.name)
        assertEquals(original.isStarred, roundTripped.isStarred)

        val ingredient = roundTripped.ingredients.single()
        assertEquals(INGREDIENT_ID, ingredient.id)
        assertEquals(INGREDIENT_CREATED_AT.toEpochSecond(), ingredient.createdAt.toEpochSecond())
        assertEquals(
            INGREDIENT_UPDATED_AT.toEpochSecond(),
            ingredient.lastUpdatedAt?.toEpochSecond()
        )

        val instruction = roundTripped.instructions.single()
        assertEquals(INSTRUCTION_ID, instruction.id)
        assertEquals(INSTRUCTION_CREATED_AT.toEpochSecond(), instruction.createdAt.toEpochSecond())
        assertEquals(
            INSTRUCTION_UPDATED_AT.toEpochSecond(),
            instruction.lastUpdatedAt?.toEpochSecond()
        )
    }

    @Test
    fun draft_survivesSerializationRoundTrip() {
        val draft = RecipeDraft.from(recipe())
            .rename("Edited cake")
            .addIngredient("Chocolate", 1f, null)
            .addInstruction("Melt the chocolate")

        val encoded = Json.encodeToString(RecipeDraft.serializer(), draft)
        val decoded = Json.decodeFromString(RecipeDraft.serializer(), encoded)

        assertEquals(draft, decoded)
    }

    @Test
    fun hasSameContentAs_ignoresKeysAndDatabaseFields() {
        val edited = RecipeDraft()
            .addIngredient("Flour", 2f, "cups")
            .addIngredient("Sugar", 1f, "cup")
            .removeIngredient(0)
            .addIngredient("Milk", 1f, "cup")
            .addInstruction("Bake")

        val persistedEquivalent = RecipeDraft.from(edited.toRecipe())

        assertTrue(edited.hasSameContentAs(persistedEquivalent))
        assertFalse(edited.hasSameContentAs(persistedEquivalent.rename("Other name")))
    }

    private fun recipe() = Recipe(
        id = RECIPE_ID,
        name = "Cake",
        ingredients = listOf(
            Ingredient(
                id = INGREDIENT_ID,
                name = "Flour",
                quantity = 2f,
                measurementUnit = "cups",
                createdAt = INGREDIENT_CREATED_AT,
                lastUpdatedAt = INGREDIENT_UPDATED_AT
            )
        ),
        instructions = listOf(
            Instruction(
                id = INSTRUCTION_ID,
                text = "Bake",
                createdAt = INSTRUCTION_CREATED_AT,
                lastUpdatedAt = INSTRUCTION_UPDATED_AT
            )
        ),
        isStarred = true
    )

    private companion object {
        const val RECIPE_ID = 42L
        const val INGREDIENT_ID = 7L
        const val INSTRUCTION_ID = 9L
        const val UNKNOWN_KEY = 999L

        val INGREDIENT_CREATED_AT: ZonedDateTime =
            ZonedDateTime.of(2020, 1, 2, 3, 4, 5, 0, ZoneId.systemDefault())
        val INGREDIENT_UPDATED_AT: ZonedDateTime =
            ZonedDateTime.of(2021, 2, 3, 4, 5, 6, 0, ZoneId.systemDefault())
        val INSTRUCTION_CREATED_AT: ZonedDateTime =
            ZonedDateTime.of(2022, 3, 4, 5, 6, 7, 0, ZoneId.systemDefault())
        val INSTRUCTION_UPDATED_AT: ZonedDateTime =
            ZonedDateTime.of(2023, 4, 5, 6, 7, 8, 0, ZoneId.systemDefault())
    }
}
