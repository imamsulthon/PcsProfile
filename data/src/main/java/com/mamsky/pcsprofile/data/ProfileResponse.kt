package com.mamsky.pcsprofile.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(

    @SerialName("id")
    val id: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("name")
    val name: String,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("city")
    val city: String,
    @SerialName("country")
    val country: String,
    @SerialName("county")
    val county: String,
    @SerialName("address_no")
    val addressNo: String,
    @SerialName("street")
    val street: String,
    @SerialName("zip_code")
    val zipCode: String,
)