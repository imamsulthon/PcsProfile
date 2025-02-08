package com.mamsky.pcsprofile.data.utils

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mamsky.pcsprofile.core.HttpException
import com.mamsky.pcsprofile.core.Result
import com.mamsky.pcsprofile.data.BuildConfig
import com.mamsky.pcsprofile.data.utils.RetrofitAdapterUtils.converterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal fun retrofit(
    chuckerInterceptor: ChuckerInterceptor,
): Retrofit = Retrofit.Builder()
    .baseUrl("https://66b197c51ca8ad33d4f482c9.mockapi.io/")
    .client(authorizedOkHttpClient(chuckerInterceptor))
    .addConverterFactory(converterFactory)
    .addCallAdapterFactory(ResultAdapterFactory())
    .build()

private fun authorizedOkHttpClient(
    chuckerInterceptor: ChuckerInterceptor,
): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor {
            it.proceed(
                it.request().newBuilder().addHeader("accept", "application/json",).build()
            )
        }.apply {
            if (BuildConfig.DEBUG) { addInterceptor(chuckerInterceptor) }
        }.build()

object RetrofitAdapterUtils {

    @OptIn(ExperimentalSerializationApi::class)
    internal val defaultJson = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    val converterFactory = defaultJson.asConverterFactory("application/json".toMediaType())
}

internal class ResultAdapterFactory : CallAdapter.Factory() {

    @Suppress("ReturnCount")
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val rawReturnType: Class<*> = getRawType(returnType)
        if (rawReturnType == Call::class.java && returnType is ParameterizedType) {
            val callInnerType: Type = getParameterUpperBound(0, returnType)
            if (getRawType(callInnerType) == Result::class.java) {
                if (callInnerType is ParameterizedType) {
                    val resultInnerType = getParameterUpperBound(0, callInnerType)
                    return ResultCallAdapter<Any?>(resultInnerType)
                }
                return ResultCallAdapter<Nothing>(Nothing::class.java)
            }
        }

        return null
    }
}

private class ResultCallAdapter<T>(private val type: Type) : CallAdapter<T, Call<Result<T>>> {
    override fun responseType(): Type = type
    override fun adapt(call: Call<T>): Call<Result<T>> = ResultCall(call)
}

internal class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, Result<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<Result<T>>) =
        proxy.enqueue(ResultCallback(this, callback))

    override fun cloneImpl(): ResultCall<T> = ResultCall(proxy.clone())

    private class ResultCallback<T>(
        private val proxy: ResultCall<T>,
        private val callback: Callback<Result<T>>
    ) : Callback<T> {

        @Suppress("UNCHECKED_CAST")
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val result = if (response.isSuccessful) {
                Result.success(
                    value = response.body() as T,
                    statusCode = response.code(),
                    statusMessage = response.message(),
                    url = call.request().url.toString()
                )
            } else {
                Result.failure(
                    HttpException(
                        statusCode = response.code(),
                        statusMessage = response.message(),
                        url = call.request().url.toString()
                    )
                )
            }
            callback.onResponse(proxy, Response.success(result))
        }

        override fun onFailure(call: Call<T>, error: Throwable) {
            val result = when (error) {
                is retrofit2.HttpException -> Result.failure<T>(
                    HttpException(
                        statusCode = error.code(),
                        statusMessage = error.message(),
                        cause = error
                    )
                )
                is IOException -> Result.failure(error)
                else -> Result.failure(error)
            }

            callback.onResponse(proxy, Response.success(result))
        }
    }

    override fun timeout(): Timeout = proxy.timeout()
}

internal abstract class CallDelegate<In, Out>(protected val proxy: Call<In>) : Call<Out> {
    abstract fun enqueueImpl(callback: Callback<Out>)
    abstract fun cloneImpl(): Call<Out>
    final override fun clone(): Call<Out> = cloneImpl()
    final override fun enqueue(callback: Callback<Out>) = enqueueImpl(callback)
    override fun execute(): Response<Out> = throw NotImplementedError()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun cancel() = proxy.cancel()
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun request(): Request = proxy.request()
}