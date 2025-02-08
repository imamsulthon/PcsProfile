package com.mamsky.pcsprofile.data

import com.mamsky.pcsprofile.core.Result
import com.mamsky.pcsprofile.data.local.DbTransactionProvider
import com.mamsky.pcsprofile.data.local.ProfileDatabase
import com.mamsky.pcsprofile.data.local.ProfileEntity
import com.mamsky.pcsprofile.data.local.toEntity
import com.mamsky.pcsprofile.data.local.toResponse
import com.mamsky.pcsprofile.data.utils.networkBoundResource
import com.mamsky.pcsprofile.data.utils.simpleCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ProfileApiService,
    private val database: ProfileDatabase,
    private val transactionProvider: DbTransactionProvider,
): ProfileRepository {

    override fun getList(caching: Boolean): Flow<Result<List<ProfileResponse>>> {
        return if (caching) networkBoundResource(
            query = {
                val result = database.profileDao.getByIds().listMap(ProfileEntity::toResponse)
                result
            },
            fetch = apiService::getList,
            saveFetchResult = {
                println("ProfileVM RepositoryImpl 0: ${it.size} $it")
                transactionProvider.runWithTransaction {
                    database.profileDao.deleteAll()
                    database.profileDao.insertAll(it.map { data -> data.toEntity() })
                }
            }
        ) else simpleCall(
            query = { flow { Result.loading(null) }},
            fetch = { apiService.getList() },
            onSuccess = {
                println("ProfileVM RepositoryImpl 1: ${it.size} $it")
                it
            }
        )
    }

    override fun getDetail(id: String, fromCache: Boolean): Flow<Result<ProfileResponse>> {
        return if (fromCache) networkBoundResource(
            query = {
                val result = database.profileDao.getById(id).map { it?.toResponse() ?: defaultItem }
                result
            },
            shouldFetch = { type ->
                type.id.equals("null", false)
            },
            fetch = { apiService.getList() },
            saveFetchResult = {
                transactionProvider.runWithTransaction {
                    val selectedItem = it.first { model -> model.id == id }
                    database.profileDao.delete(selectedItem.id)
                    database.profileDao.insert(selectedItem.toEntity())
                }
            }
        ) else
            simpleCall(
                query = { flow { Result.loading(null) }},
                fetch = { apiService.getList() },
                onSuccess = {
                    val item =  it.first { data -> data.id == id }
                    println("ProfileVM RepositoryImpl 4: ${it.size} $item")
                    item
            }
        )
    }


    private fun <T, R> Flow<List<T>>.listMap(
        transform: suspend (T) -> R
    ) = map { list -> list.map { transform(it) } }

    private val defaultItem = ProfileResponse("null", "", "", "", "", "", "", "", "", "")

}