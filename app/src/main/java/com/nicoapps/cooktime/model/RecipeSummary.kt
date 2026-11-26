package com.nicoapps.cooktime.model

import java.time.ZonedDateTime

/**
 * What recipe lists show: a recipe without its ingredients and instructions, which lists don't
 * load.
 */
data class RecipeSummary(
    val id: Long,
    val name: String,
    val isStarred: Boolean,
    val createdAt: ZonedDateTime
)
