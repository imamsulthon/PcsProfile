package com.mamsky.pcsprofile.data

import com.mamsky.pcsprofile.core.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getList(caching: Boolean): Flow<Result<List<ProfileResponse>>>

    fun getDetail(id: String, fromCache: Boolean): Flow<Result<ProfileResponse>>
}