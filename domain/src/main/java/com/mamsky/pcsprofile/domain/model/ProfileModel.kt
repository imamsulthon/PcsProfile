package com.mamsky.pcsprofile.domain.model

data class ProfileModel(
    val id: String,
    val createdAt: String,
    val name: String,
    val avatar: String,
    val city: String,
    val country: String,
    val county: String,
    val addressNo: String,
    val street: String,
    val zipCode: String,
)