package com.nicoapps.cooktime.ui.screens.recipe.edit.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.ui.defaultAnimationSpec

@Composable
fun ViewRecipeAppTopBarTitle(
    modifier: Modifier = Modifier,
    titleText: String,
    isEditing: Boolean,
    onEditClick: () -> Unit
) {
    Row(
        modifier = modifier
            .animateContentSize(animationSpec = defaultAnimationSpec()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = titleText
        )

        AnimatedVisibility(
            visible = isEditing,
            enter = scaleIn(
                animationSpec = defaultAnimationSpec(delayMillis = 150)
            ),
            exit = scaleOut()
        ) {
            Icon(
                modifier = Modifier.clickable { onEditClick() },
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Localized description"
            )
        }
    }
}