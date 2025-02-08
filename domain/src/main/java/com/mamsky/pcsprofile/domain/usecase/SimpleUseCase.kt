package com.mamsky.pcsprofile.domain.usecase

import com.mamsky.pcsprofile.core.Result
import com.mamsky.pcsprofile.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface SimpleUseCase {

    fun getList(): Flow<Result<List<ProfileModel>>>

    fun getDetail(id: String): Flow<Result<ProfileModel>>

}