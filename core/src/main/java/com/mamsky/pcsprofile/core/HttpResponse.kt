package com.mamsky.pcsprofile.core

internal interface HttpResponse {
    val statusCode: Int
    val statusMessage: String?
    val url: String?
}
