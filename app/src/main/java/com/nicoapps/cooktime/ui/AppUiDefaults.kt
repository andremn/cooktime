package com.nicoapps.cooktime.ui

import androidx.compose.animation.core.tween
fun<T> defaultAnimationSpec() = tween<T>(
    durationMillis = 350
)