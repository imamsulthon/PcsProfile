package com.mamsky.pcsprofile.data.utils

import com.mamsky.pcsprofile.core.Result
import com.mamsky.pcsprofile.core.isFailure
import com.mamsky.pcsprofile.core.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


inline fun <ResultType, RequestType> simpleCall(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> Result<RequestType>,
    crossinline onSuccess: suspend (RequestType) -> ResultType,
): Flow<Result<ResultType>> = flow {
    emit(Result.loading())
    val response = fetch()
    val flow = when {
        response.isSuccess() -> {
            val successData = onSuccess.invoke(response.value)
            emit(Result.success(successData))
            query().map { Result.success(successData) }
        }
        response.isFailure() -> {
            val throwable = response.error
            emit(Result.failure(throwable))
            query().map { Result.failure(throwable, it) }
        }
        else -> {
            emit(Result.failure(response.error))
            error("MESSAGE_UNHANDLED_STATE $response")
        }
    }
    emitAll(flow)
}

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> Result<RequestType>,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
): Flow<Result<ResultType>> = flow {
    emit(Result.loading())
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Result.Loading(data))
        val response = fetch()
        when {
            response.isSuccess() -> {
                saveFetchResult(response.value)
                query().map { Result.success(it) }
            }
            response.isFailure() -> {
                val throwable = response.error
                query().map { Result.failure(throwable, it) }
            }
            else -> error("MESSAGE_UNHANDLED_STATE $response")
        }
    } else {
        query().map { Result.success(it) }
    }

    emitAll(flow)
}
