package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import kotlin.math.min

@Composable
fun AnimatedLineThroughText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    isLinedThrough: Boolean = true,
    animationSpec: AnimationSpec<Int> = tween(
        durationMillis = min(ANIM_MIN_DURATION, text.length.times(ANIM_DURATION_FACTOR)),
        easing = FastOutLinearInEasing
    ),
    lineThroughStyle: SpanStyle = SpanStyle(),
    style: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign? = null
) {
    var textToDisplay by remember {
        mutableStateOf(AnnotatedString(""))
    }

    val length = remember {
        Animatable(initialValue = 0, typeConverter = Int.VectorConverter)
    }

    LaunchedEffect(length.value) {
        textToDisplay = text.buildStrikethrough(length.value, lineThroughStyle)
    }

    LaunchedEffect(isLinedThrough) {
        when {
            isLinedThrough -> length.animateTo(text.length, animationSpec)
            !isLinedThrough -> length.animateTo(0, animationSpec)
            else -> length.snapTo(0)
        }
    }

    LaunchedEffect(text) {
        when {
            isLinedThrough && text.length == length.value -> {
                textToDisplay = text.buildStrikethrough(length.value, lineThroughStyle)
            }

            isLinedThrough && text.length != length.value -> {
                length.snapTo(text.length)
            }

            else -> textToDisplay = text
        }
    }

    Text(
        text = textToDisplay,
        modifier = modifier,
        style = style,
        textAlign = textAlign
    )
}

fun AnnotatedString.buildStrikethrough(length: Int, style: SpanStyle) = buildAnnotatedString {
    append(this@buildStrikethrough)

    val strikethroughStyle = style.copy(textDecoration = TextDecoration.LineThrough)

    addStyle(strikethroughStyle, 0, length)
}

const val ANIM_MIN_DURATION = 200

const val ANIM_DURATION_FACTOR = 30