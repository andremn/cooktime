package com.nicoapps.cooktime.data

import org.junit.Assert.assertEquals
import org.junit.Test

class ContainsPatternTest {

    @Test
    fun plainText_isWrappedInWildcards() {
        assertEquals("%bolo%", containsPattern("bolo"))
    }

    @Test
    fun wildcards_areEscaped() {
        assertEquals("%100\\%%", containsPattern("100%"))
        assertEquals("%a\\_b%", containsPattern("a_b"))
    }

    @Test
    fun escapeCharacter_isEscaped() {
        assertEquals("%a\\\\b%", containsPattern("a\\b"))
    }
}
