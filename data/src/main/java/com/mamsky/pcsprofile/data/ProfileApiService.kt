package com.mamsky.pcsprofile.data

import com.mamsky.pcsprofile.core.Result
import retrofit2.http.GET

interface ProfileApiService {

    @GET("getData/test")
    suspend fun getList() : Result<List<ProfileResponse>>

}