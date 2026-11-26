package com.nicoapps.cooktime.ui.extensions

import kotlinx.coroutines.CancellationException

/**
 * Runs [write] and reports any failure through [onFailure], returning whether it completed.
 *
 * Cancellation is rethrown: a write cancelled by its scope is not a user-facing error.
 */
suspend fun runWrite(
    onFailure: () -> Unit,
    write: suspend () -> Unit
): Boolean =
    try {
        write()
        true
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        onFailure()
        false
    }
