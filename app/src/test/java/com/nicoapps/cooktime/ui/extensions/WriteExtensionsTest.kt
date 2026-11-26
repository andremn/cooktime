package com.nicoapps.cooktime.ui.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class WriteExtensionsTest {

    @Test
    fun successfulWrite_returnsTrueAndDoesNotReportFailure() = runBlocking {
        var writeRan = false
        var failureReported = false

        val completed = runWrite(onFailure = { failureReported = true }) {
            writeRan = true
        }

        assertTrue(writeRan)
        assertTrue(completed)
        assertFalse(failureReported)
    }

    @Test
    fun failingWrite_returnsFalseAndReportsFailure() = runBlocking {
        var failureReported = false

        val completed = runWrite(onFailure = { failureReported = true }) {
            throw IllegalStateException("write failed")
        }

        assertFalse(completed)
        assertTrue(failureReported)
    }

    @Test
    fun cancelledWrite_isRethrownWithoutReportingFailure() {
        var failureReported = false

        assertThrows(CancellationException::class.java) {
            runBlocking {
                runWrite(onFailure = { failureReported = true }) {
                    throw CancellationException("cancelled")
                }
            }
        }

        assertFalse(failureReported)
    }
}
