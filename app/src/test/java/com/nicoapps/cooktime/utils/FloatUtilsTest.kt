package com.nicoapps.cooktime.utils

import com.nicoapps.cooktime.utils.FloatUtils.formatQuantity
import com.nicoapps.cooktime.utils.FloatUtils.parseQuantity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Locale

/** `isSingularQuantity` uses android.icu, so it's covered by the instrumented `PluralQuantityTest`. */
class FloatUtilsTest {

    private val portuguese = Locale.forLanguageTag("pt-BR")

    @Test
    fun parseQuantity_acceptsCommaAndDot() {
        assertEquals(0.5f, parseQuantity("0,5"))
        assertEquals(0.5f, parseQuantity("0.5"))
        assertEquals(2f, parseQuantity(" 2 "))
    }

    @Test
    fun parseQuantity_rejectsInvalidOrNonPositiveValues() {
        assertNull(parseQuantity(""))
        assertNull(parseQuantity("abc"))
        assertNull(parseQuantity("1,000.5"))
        assertNull(parseQuantity("0"))
        assertNull(parseQuantity("-1"))
    }

    @Test
    fun formatQuantity_usesLocaleDecimalSeparator() {
        assertEquals("0,5", 0.5f.formatQuantity(portuguese))
        assertEquals("0.5", 0.5f.formatQuantity(Locale.US))
    }

    @Test
    fun formatQuantity_dropsTrailingZerosAndGrouping() {
        assertEquals("2", 2f.formatQuantity(portuguese))
        assertEquals("1500", 1500f.formatQuantity(Locale.US))
        assertEquals("0,125", 0.125f.formatQuantity(portuguese))
    }

    @Test
    fun formatThenParse_keepsCommonRecipeFractions() {
        for (quantity in listOf(0.125f, 0.0625f, 0.375f, 1.5f, 0.333f)) {
            for (locale in listOf(portuguese, Locale.US)) {
                assertEquals(quantity, parseQuantity(quantity.formatQuantity(locale)))
            }
        }
    }
}
