package com.mamsky.pcsprofile.domain.mapper

import com.mamsky.pcsprofile.data.ProfileResponse
import com.mamsky.pcsprofile.domain.model.ProfileModel


fun ProfileResponse.toModel() = ProfileModel(
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