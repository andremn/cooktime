package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import com.nicoapps.cooktime.ui.components.AppTextField
import com.nicoapps.cooktime.ui.components.appColors
import com.nicoapps.cooktime.ui.defaultAnimationSpec
import com.nicoapps.cooktime.ui.extensions.animatePlacement

@Composable
fun RecipeInstructionGridItem(
    modifier: Modifier = Modifier,
    index: Int,
    text: String,
    isEditing: Boolean,
    onTextChange: (String) -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .animatePlacement(),
            text = "${index.inc()}.",
            textAlign = TextAlign.Left
        )

        AppTextField(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .animatePlacement(),
            value = text,
            enabled = isEditing,
            onValueChange = {
                onTextChange(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrect = true
            ),
            colors = TextFieldDefaults.appColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start)
        )

        AnimatedVisibility(
            visible = isEditing,
            enter = slideInHorizontally(
                animationSpec = defaultAnimationSpec()
            ) { width -> width } + fadeIn(),
            exit = slideOutHorizontally(
                animationSpec = defaultAnimationSpec()
            ) { width -> width } + fadeOut()
        ) {
            IconButton(
                onClick = {
                    onRemoveClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
                )
            }
        }
    }
}