package com.nicoapps.cooktime.debug

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nicoapps.cooktime.LocalRepository
import com.nicoapps.cooktime.data.RecipeRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Adds [SampleRecipes] that aren't in the database yet, matched by name, so running it again
 * only restores the ones that were deleted. Writes through the repository, so the samples keep
 * working when the schema changes.
 */
@AndroidEntryPoint
class SampleRecipesReceiver : BroadcastReceiver() {

    @Inject
    @LocalRepository
    lateinit var recipeRepository: RecipeRepository

    @Inject
    lateinit var databaseCoroutineScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()

        databaseCoroutineScope.launch {
            try {
                val existingNames = recipeRepository.getAll().first().map { it.name }.toSet()
                val missing = SampleRecipes.all.filterNot { it.name in existingNames }

                missing.forEach { recipeRepository.save(it) }

                Log.i(TAG, "Added ${missing.size} sample recipe(s): ${missing.map { it.name }}")
            } finally {
                pendingResult.finish()
            }
        }
    }

    private companion object {
        const val TAG = "SampleRecipes"
    }
}
