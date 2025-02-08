package com.mamsky.pcsprofile.core

import kotlinx.coroutines.flow.Flow

interface ResultHandler<T> {
    fun onLoading(block: (T?) -> Unit)
    fun onSuccess(block: (T) -> Unit)
    fun onFailure(block: (Throwable) -> Unit)
}

fun <T> Result<T>.handle(builder: ResultHandler<T>.() -> Unit) {
    val resultHandler = object : ResultHandler<T> {
        override fun onLoading(block: (T?) -> Unit) {
            if (isLoading()) block(value)
        }

        override fun onSuccess(block: (T) -> Unit) {
            if (isSuccess()) block(value)
        }

        override fun onFailure(block: (Throwable) -> Unit) {
            if (isFailure()) block(error)
        }
    }

    builder(resultHandler)
}

suspend fun <T> Flow<Result<T>>.handle(builder: ResultHandler<T>.() -> Unit) =
    collect { result -> result.handle(builder = builder) }
