package com.nicoapps.cooktime.utils

import android.icu.text.PluralRules
import java.text.NumberFormat
import java.util.Locale

object FloatUtils {
    /**
     * Enough digits to show exactly what the user typed (e.g. 0.0625 for 1/16) while hiding
     * Float noise, so the edit dialog prefilled with this text saves the same value back.
     */
    private const val MAX_QUANTITY_FRACTION_DIGITS = 6

    /** Formats an ingredient quantity with the locale's decimal separator, e.g. "0,5" in pt. */
    fun Float.formatQuantity(locale: Locale = Locale.getDefault()): String =
        NumberFormat.getNumberInstance(locale).apply {
            maximumFractionDigits = MAX_QUANTITY_FRACTION_DIGITS
            isGroupingUsed = false
        }.format(this)

    /**
     * Parses a quantity typed by the user, accepting either "," or "." as the decimal
     * separator regardless of locale. Returns null for anything that isn't a positive number.
     */
    fun parseQuantity(text: String): Float? {
        val normalized = text.trim().replace(',', '.')

        if (normalized.count { it == '.' } > 1) {
            return null
        }

        return normalized.toFloatOrNull()?.takeIf { it.isFinite() && it > 0f }
    }

    /** Whether the quantity takes the singular form ("1 unit", "0,5 unidade") in [locale]. */
    fun Float.isSingularQuantity(locale: Locale = Locale.getDefault()): Boolean =
        PluralRules.forLocale(locale).select(toDouble()) == PluralRules.KEYWORD_ONE
}
