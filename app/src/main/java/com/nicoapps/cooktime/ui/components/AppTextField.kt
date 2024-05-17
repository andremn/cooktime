package com.nicoapps.cooktime.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.appColors(),
    textStyle: TextStyle = LocalTextStyle.current,
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
        textStyle = textStyle,
        singleLine = singleLine,
        value = value,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        readOnly = readOnly,
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
    disabledIndicatorColor: Color = MaterialTheme.colorScheme.onSurface,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surface,
    disabledTextColor: Color = MaterialTheme.colorScheme.onSurface
) =
    colors(
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        errorContainerColor = errorContainerColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor,
        disabledContainerColor = disabledContainerColor,
        disabledTextColor = disabledTextColor
    )