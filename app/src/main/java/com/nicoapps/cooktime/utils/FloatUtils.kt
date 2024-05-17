package com.nicoapps.cooktime.utils

object FloatUtils {
    fun Float.formatQuantity(): String {
        if (this == this.toLong().toFloat()) {
            return String.format("%d", toLong())
        }

        return String.format("%s", this)
    }
}