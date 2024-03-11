package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.TopAppBar
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
    @Composable
    fun TopAppBarTitle() {
        Text(
            text = title
        )
    }

    @Composable
    fun NavigationIcon() {
        IconButton(
            onClick = { onNavigationIconClick() }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
    }

    @Composable
    fun topAppBarColors() =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )

    AnimatedContent(
        targetState = showActions,
        label = "appTitleTopBarAnimation",
        transitionSpec = {
            if (initialState && !targetState) {
                slideInVertically(
                    animationSpec = defaultAnimationSpec()
                ) { height -> height } + fadeIn(
                    animationSpec = defaultAnimationSpec()
                ) togetherWith
                        slideOutVertically(
                            animationSpec = defaultAnimationSpec()
                        ) { height -> -height } + fadeOut(
                    animationSpec = defaultAnimationSpec()
                )
            } else {
                slideInVertically(
                    animationSpec = defaultAnimationSpec()
                ) { height -> -height } + fadeIn(
                    animationSpec = defaultAnimationSpec()
                ) togetherWith
                        slideOutVertically(
                            animationSpec = defaultAnimationSpec()
                        ) { height -> height } + fadeOut(
                    animationSpec = defaultAnimationSpec()
                )
            }.using(
                SizeTransform(clip = true)
            )
        }
    ) { displayActions ->
        if (displayActions) {
            TopAppBar(
                title = { TopAppBarTitle() },
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
                navigationIcon = { NavigationIcon() },
                colors = topAppBarColors()
            )
        } else {
            CenterAlignedTopAppBar(
                title = { TopAppBarTitle() },
                modifier = modifier.fillMaxWidth(),
                navigationIcon = { NavigationIcon() },
                colors = topAppBarColors()
            )
        }
    }
}