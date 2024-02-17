package com.nicoapps.cooktime.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    singleLine: Boolean = false,
    isError: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.appColors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    supportingText: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        placeholder = placeholder,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        supportingText = supportingText,
        colors = colors,
        singleLine = singleLine,
        value = value,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange
    )
}

@Composable
fun TextFieldDefaults.appColors(
    focusedContainerColor: Color = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor: Color = MaterialTheme.colorScheme.surface,
    errorContainerColor: Color = MaterialTheme.colorScheme.surface,
    focusedIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledIndicatorColor: Color = MaterialTheme.colorScheme.onSurface
) =
    colors(
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        errorContainerColor = errorContainerColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor
    )