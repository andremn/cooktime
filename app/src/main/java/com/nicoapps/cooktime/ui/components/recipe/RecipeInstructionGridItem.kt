package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import com.nicoapps.cooktime.R
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
        // Outside editing, the number and the text are read as one item.
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = isEditing.not()) {},
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .animatePlacement(),
            text = "${index.inc()}.",
            textAlign = TextAlign.Left
        )

        if (isEditing) {
            AppTextField(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .animatePlacement(),
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true
                ),
                colors = TextFieldDefaults.appColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Start)
            )
        } else {
            // Plain text, with the text field's size and padding, so entering and leaving editing
            // doesn't move the list.
            Box(
                modifier = Modifier
                    .fillMaxWidth(.9f)
                    .heightIn(min = TextFieldDefaults.MinHeight)
                    .padding(TextFieldDefaults.contentPaddingWithoutLabel())
                    .animatePlacement(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = text,
                    style = LocalTextStyle.current.copy(textAlign = TextAlign.Start)
                )
            }
        }

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
                    contentDescription = stringResource(
                        R.string.cd_remove_instruction,
                        index.inc()
                    )
                )
            }
        }
    }
}