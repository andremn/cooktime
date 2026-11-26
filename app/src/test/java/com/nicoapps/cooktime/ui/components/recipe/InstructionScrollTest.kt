package com.nicoapps.cooktime.ui.components.recipe

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InstructionScrollTest {

    @Test
    fun unchangedList_isNotAnAppend() {
        assertFalse(wasInstructionAppended(listOf(0L, 1L, 2L), listOf(0L, 1L, 2L)))
    }

    @Test
    fun removedInstruction_isNotAnAppend() {
        assertFalse(wasInstructionAppended(listOf(0L, 1L, 2L), listOf(0L, 2L)))
    }

    @Test
    fun appendedInstruction_isAnAppend() {
        assertTrue(wasInstructionAppended(listOf(0L, 1L), listOf(0L, 1L, 2L)))
    }

    @Test
    fun firstInstruction_isAnAppend() {
        assertTrue(wasInstructionAppended(emptyList(), listOf(0L)))
    }

    @Test
    fun replacingAnInstruction_isNotAnAppend() {
        assertFalse(wasInstructionAppended(listOf(0L, 1L), listOf(0L, 2L)))
    }

    @Test
    fun severalAppendedInstructions_areAnAppend() {
        assertTrue(wasInstructionAppended(listOf(0L), listOf(0L, 1L, 2L)))
    }

    @Test
    fun movingAnInstruction_isNotAnAppend() {
        assertFalse(wasInstructionAppended(listOf(0L, 1L, 2L), listOf(1L, 0L, 2L)))
    }

    @Test
    fun insertingInTheMiddle_isNotAnAppend() {
        assertFalse(wasInstructionAppended(listOf(0L, 2L), listOf(0L, 1L, 2L)))
    }
}
