package com.nicoapps.cooktime.model

import java.time.ZonedDateTime

data class Instruction(
    val id: Long = 0,
    val text: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val lastUpdatedAt: ZonedDateTime? = null
)
