package com.nicoapps.cooktime.ui.screens.recipe.create.steps

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.components.AppTextField

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewRecipeNameStep(
    modifier: Modifier = Modifier,
    recipeName: String,
    onCurrentTitleChanged: (String) -> Unit,
    onRecipeNameChanged: (String) -> Unit
) {
    val context = LocalContext.current
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onCurrentTitleChanged(
            context.resources.getString(R.string.new_recipe_name_step_title)
        )
    }

    AppTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.text_input_padding)),
        placeholder = { Text(stringResource(R.string.new_recipe_name_step_placeholder)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences
        ),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(text = stringResource(id = R.string.new_recipe_name_empty_error))
            }
        },
        singleLine = true,
        value = recipeName,
        onValueChange = {
            isError = it.isEmpty()
            onRecipeNameChanged(it)
        }
    )
}