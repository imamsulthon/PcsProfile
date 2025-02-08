package com.mamsky.pcsprofile.data.local

import com.mamsky.pcsprofile.data.ProfileResponse


fun ProfileEntity.toResponse() = ProfileResponse(
    id = id,
    createdAt = createdAt,
    name = name,
    avatar = avatar,
    city = city,
    county = county,
    country = country,
    street = street,
    addressNo = addressNo,
    zipCode = zipCode
)

fun ProfileResponse.toEntity() = ProfileEntity(
    id = id,
    createdAt = createdAt,
    name = name,
    avatar = avatar,
    city = city,
    county = county,
    country = country,
    street = street,
    addressNo = addressNo,
    zipCode = zipCode
)