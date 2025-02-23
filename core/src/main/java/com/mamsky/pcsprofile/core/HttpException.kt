package com.mamsky.pcsprofile.core

class HttpException(
    val statusCode: Int,
    val statusMessage: String? = null,
    val url: String? = null,
    cause: Throwable? = null
) : Exception(null, cause) {
    override fun toString(): String {
        return "$statusCode $statusMessage $url $cause"
    }
}
