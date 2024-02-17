package com.nicoapps.cooktime.model

import java.time.ZonedDateTime

data class Instruction(
    val id: Int = 0,
    val text: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val lastUpdatedAt: ZonedDateTime? = null
)
