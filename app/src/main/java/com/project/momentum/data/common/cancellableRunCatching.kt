package com.project.momentum.data.common

import kotlinx.coroutines.CancellationException

/** Wraps an operation failure without converting coroutine cancellation into a business result. */
inline fun <T> cancellableRunCatching(block: () -> T): Result<T> = try {
    Result.success(block())
} catch (cancelled: CancellationException) {
    throw cancelled
} catch (failure: Exception) {
    Result.failure(failure)
}
