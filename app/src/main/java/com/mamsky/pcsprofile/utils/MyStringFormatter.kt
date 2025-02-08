package com.mamsky.pcsprofile.utils

import java.text.SimpleDateFormat
import java.util.Locale

private const val DATE_SO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun String?.formatDate(inputPattern: String = DATE_SO8601, outputPattern: String = "MMMM dd, yyyy"): String {
    return try {
        if (this.isNullOrEmpty()) return ""
        val input = SimpleDateFormat(inputPattern, Locale.ROOT)
        val output = SimpleDateFormat(outputPattern, Locale.US)
        output.format(input.parse(this))
    } catch (e: Exception) {
        e.printStackTrace()
        "Invalid Date"
    }
}