/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.HttpException
import retrofit2.Response

fun Deferred<Response<*>>.asBooleanDeferred(): Deferred<Boolean> {
    val result = CompletableDeferred<Boolean>()

    invokeOnCompletion {
        if (result.isCancelled) {
            cancel()

            if (it is CancellationException)
                return@invokeOnCompletion
        }

        if (it != null) {
            result.completeExceptionally(it)
            return@invokeOnCompletion
        }

        val response = getCompleted()

        when {
            response.isSuccessful -> result.complete(true)
            response.code() == 404 -> result.complete(false)
            else -> result.completeExceptionally(HttpException(response))
        }
    }

    return result
}

fun Deferred<Response<*>>.asUnitDeferred(): Deferred<Unit> {
    val result = CompletableDeferred<Unit>()

    invokeOnCompletion {
        if (result.isCancelled) {
            cancel()

            if (it is CancellationException)
                return@invokeOnCompletion
        }

        if (it != null) {
            result.completeExceptionally(it)
            return@invokeOnCompletion
        }

        val response = getCompleted()

        when {
            response.isSuccessful -> result.complete(Unit)
            else -> result.completeExceptionally(HttpException(response))
        }
    }

    return result
}

