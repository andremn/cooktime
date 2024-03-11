package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.defaultAnimationSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTitleTopBar(
    modifier: Modifier = Modifier,
    showActions: Boolean,
    title: String,
    onNavigationIconClick: () -> Unit,
    actions: (@Composable (RowScope) -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier.fillMaxWidth(),
        actions = {
            AnimatedVisibility(
                visible = showActions,
                enter = slideInVertically(
                    animationSpec = defaultAnimationSpec()
                ) + fadeIn(
                    animationSpec = defaultAnimationSpec()
                ),
                exit = slideOutVertically(
                    animationSpec = defaultAnimationSpec()
                ) + fadeOut(
                    animationSpec = defaultAnimationSpec()
                )
            ) {
                Row {
                    actions?.invoke(this)
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { onNavigationIconClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}