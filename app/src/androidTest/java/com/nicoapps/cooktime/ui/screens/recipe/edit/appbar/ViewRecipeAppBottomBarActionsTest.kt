package com.nicoapps.cooktime.ui.screens.recipe.edit.appbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nicoapps.cooktime.R
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewRecipeAppBottomBarActionsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun editingSwapsEditAndDeleteForSaveAndDiscard() {
        var isEditing by mutableStateOf(false)

        composeTestRule.setContent {
            ViewRecipeAppBottomBarActions(
                isEditing = isEditing,
                isStarred = false,
                isEditDoneEnabled = true,
                isSaving = false,
                onEditClick = { isEditing = true },
                onDeleteClick = {},
                onStarClick = {},
                onEditDoneClick = { isEditing = false },
                onEditCancelClick = { isEditing = false }
            )
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_edit_recipe))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_delete_recipe))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_save_changes))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_edit_recipe))
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_save_changes))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_discard_changes))
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_edit_recipe))
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_delete_recipe))
            .assertDoesNotExist()
    }

    @Test
    fun startRecipeFabIsHiddenWhileEditing() {
        var isEditing by mutableStateOf(false)

        composeTestRule.setContent {
            ViewRecipeAppBottomBarFab(
                isVisible = isEditing.not(),
                onClick = {}
            )
        }
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText(
                context.getString(R.string.start_recipe_button_text),
                useUnmergedTree = true
            )
            .assertIsDisplayed()

        isEditing = true

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText(
                context.getString(R.string.start_recipe_button_text),
                useUnmergedTree = true
            )
            .assertDoesNotExist()
    }

    @Test
    fun saveActionIsDisabledExactlyWhenChangesCannotBeSaved() {
        var isEditDoneEnabled by mutableStateOf(false)
        var isEditDoneClicked = false

        composeTestRule.setContent {
            ViewRecipeAppBottomBarActions(
                isEditing = true,
                isStarred = false,
                isEditDoneEnabled = isEditDoneEnabled,
                isSaving = false,
                onEditClick = {},
                onDeleteClick = {},
                onStarClick = {},
                onEditDoneClick = { isEditDoneClicked = true },
                onEditCancelClick = {}
            )
        }

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_save_changes))
            .performClick()
        assertFalse(isEditDoneClicked)

        isEditDoneEnabled = true

        composeTestRule
            .onNodeWithContentDescription(context.getString(R.string.cd_save_changes))
            .performClick()
        assertTrue(isEditDoneClicked)
    }
}
