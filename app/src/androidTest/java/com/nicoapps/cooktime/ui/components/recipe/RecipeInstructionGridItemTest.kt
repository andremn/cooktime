package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nicoapps.cooktime.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeInstructionGridItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun outsideEditingTheInstructionIsPlainTextReadTogetherWithItsNumber() {
        composeTestRule.setContent {
            RecipeInstructionGridItem(
                index = 1,
                text = INSTRUCTION,
                isEditing = false,
                onTextChange = {},
                onRemoveClick = {}
            )
        }

        // A single node holds both the number and the text, and it can't be edited.
        composeTestRule
            .onNodeWithText(INSTRUCTION, substring = true)
            .assertIsDisplayed()
            .assert(hasText("2.", substring = true))
            .assert(hasSetTextAction().not())
        composeTestRule
            .onNodeWithContentDescription(removeDescription(index = 1))
            .assertDoesNotExist()
    }

    @Test
    fun editingTurnsTheInstructionIntoAFieldThatReportsChanges() {
        var text by mutableStateOf(INSTRUCTION)

        composeTestRule.setContent {
            RecipeInstructionGridItem(
                index = 0,
                text = text,
                isEditing = true,
                onTextChange = { text = it },
                onRemoveClick = {}
            )
        }

        composeTestRule
            .onNodeWithText(INSTRUCTION)
            .assert(hasSetTextAction())
            .performTextReplacement(EDITED_INSTRUCTION)

        assertEquals(EDITED_INSTRUCTION, text)
        composeTestRule.onNodeWithText(EDITED_INSTRUCTION).assertIsDisplayed()
    }

    @Test
    fun editingShowsTheRemoveButton() {
        var removed = false

        composeTestRule.setContent {
            RecipeInstructionGridItem(
                index = 0,
                text = INSTRUCTION,
                isEditing = true,
                onTextChange = {},
                onRemoveClick = { removed = true }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(removeDescription(index = 0))
            .performClick()

        assertTrue(removed)
    }

    private fun removeDescription(index: Int) =
        context.getString(R.string.cd_remove_instruction, index.inc())

    private companion object {
        const val INSTRUCTION = "Bata os ovos com o sal."
        const val EDITED_INSTRUCTION = "Bata os ovos com sal e pimenta."
    }
}
