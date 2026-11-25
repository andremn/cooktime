package com.nicoapps.cooktime.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicoapps.cooktime.utils.FloatUtils.isSingularQuantity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class PluralQuantityTest {

    private val portuguese = Locale.forLanguageTag("pt-BR")

    @Test
    fun portuguese_usesSingularUpToOne() {
        assertTrue(0.5f.isSingularQuantity(portuguese))
        assertTrue(1f.isSingularQuantity(portuguese))
        assertFalse(2f.isSingularQuantity(portuguese))
    }

    @Test
    fun english_usesSingularOnlyForOne() {
        assertTrue(1f.isSingularQuantity(Locale.US))
        assertFalse(0.5f.isSingularQuantity(Locale.US))
        assertFalse(2f.isSingularQuantity(Locale.US))
    }
}
