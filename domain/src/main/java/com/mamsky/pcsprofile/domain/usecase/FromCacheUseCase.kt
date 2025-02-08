package com.mamsky.pcsprofile.domain.usecase

import com.mamsky.pcsprofile.core.Result
import com.mamsky.pcsprofile.data.ProfileRepository
import com.mamsky.pcsprofile.data.ProfileResponse
import com.mamsky.pcsprofile.domain.mapper.toModel
import com.mamsky.pcsprofile.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FromCacheUseCase @Inject constructor(
    private val repository: ProfileRepository
): SimpleUseCase {

    override fun getList(): Flow<Result<List<ProfileModel>>> {
        return repository.getList(true).map {
            when(it) {
                is Result.Success -> Result.success(it.value.map(ProfileResponse::toModel))
                is Result.Failure -> Result.failure(it.error)
                is Result.Loading -> Result.loading(null)
            }
        }
    }

    override fun getDetail(id: String): Flow<Result<ProfileModel>> {
        return repository.getDetail(id, true).map {
            when(it) {
                is Result.Success -> Result.success(it.value.toModel())
                is Result.Failure -> Result.failure(it.error)
                is Result.Loading -> Result.loading(null)
            }
        }
    }
}