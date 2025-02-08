package com.mamsky.pcsprofile.data.remote

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mamsky.pcsprofile.data.ProfileApiService
import com.mamsky.pcsprofile.data.utils.retrofit
import retrofit2.create
import javax.inject.Inject

class CollectedApiService @Inject constructor(
    chuckerInterceptor: ChuckerInterceptor
) {
    private val retrofit = retrofit(chuckerInterceptor)
    val profileApiService: ProfileApiService by lazy { retrofit.create() }

}