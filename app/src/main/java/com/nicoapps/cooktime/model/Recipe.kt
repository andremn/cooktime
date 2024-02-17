package com.nicoapps.cooktime.model

import java.time.ZonedDateTime

data class Recipe(
    val id: Int = 0,
    val name: String,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val lastUpdatedAt: ZonedDateTime? = null
)