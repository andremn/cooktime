package com.nicoapps.cooktime.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AllDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    route: String,
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    navigateToSettings: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {
    ModalDrawerSheet(
        modifier = modifier
    ) {
        DrawerHeader(modifier)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding)))
        NavigationDrawerItem(
            label = {
                Text(
                    text = stringResource(id = R.string.home_title),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            selected = route == AllDestinations.HOME,
            onClick = {
                navigateToHome()
                closeDrawer()
            },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            shape = MaterialTheme.shapes.small
        )

        NavigationDrawerItem(
            label = {
                Text(
                    text = stringResource(id = R.string.settings_title),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            selected = route == AllDestinations.SETTINGS,
            onClick = {
                navigateToSettings()
                closeDrawer()
            },
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            shape = MaterialTheme.shapes.small
        )
    }
}


@Composable
fun DrawerHeader(modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(dimensionResource(id = R.dimen.header_padding))
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.drawable.profile_picture),
            contentDescription = stringResource(id = R.string.user_profile_content_description),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(dimensionResource(id = R.dimen.header_image_size))
                .clip(CircleShape)
        )

        Spacer(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding))
        )

        Column(
            modifier = modifier
        ) {

            Text(
                text = stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )

            Text(
                text = "3 minutes ago",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Preview
@Composable
fun DrawerHeaderPreview() {
    AppDrawer(modifier = Modifier, route = AllDestinations.HOME)
}