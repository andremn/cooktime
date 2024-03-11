package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavGraphTopBarContentType
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.components.search.SearchRecipesBar
import com.nicoapps.cooktime.ui.defaultAnimationSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    appNavGraphState: AppNavGraphState,
    drawerState: DrawerState,
    appNavigationActions: AppNavigationActions,
    coroutineScope: CoroutineScope,
    onSearchBarActiveChanged: (Boolean) -> Unit
) {
    AnimatedContent(
        targetState = appNavGraphState.topBar.contentType,
        label = "topAppBarAnimation",
        transitionSpec = {
            if (initialState == AppNavGraphTopBarContentType.TITLE_ONLY
                && targetState == AppNavGraphTopBarContentType.SEARCH_BAR
            ) {
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
    ) { contentType ->
        if (contentType == AppNavGraphTopBarContentType.TITLE_ONLY) {
            AppTitleTopBar(
                showActions = appNavGraphState.topBar.showActions,
                title = appNavGraphState.topBar.title,
                onNavigationIconClick = { appNavigationActions.navigateBack() },
                actions = appNavGraphState.topBar.actions
            )
        } else {
            SearchRecipesBar(
                modifier = modifier
                    .padding(bottom = dimensionResource(id = R.dimen.search_bar_padding)),
                onActiveChange = { onSearchBarActiveChanged(it) },
                onRecipeSelected = {
                    appNavigationActions.navigateToViewRecipe(it)
                },
                placeholder = {
                    Text(
                        text = appNavGraphState.topBar.title
                    )
                },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}