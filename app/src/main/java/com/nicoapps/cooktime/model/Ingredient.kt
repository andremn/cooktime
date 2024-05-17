package com.nicoapps.cooktime.model

import java.time.ZonedDateTime

data class Ingredient(
    val id: Long = 0,
    val name: String,
    val quantity: Float,
    val measurementUnit: String?,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val lastUpdatedAt: ZonedDateTime? = null
)