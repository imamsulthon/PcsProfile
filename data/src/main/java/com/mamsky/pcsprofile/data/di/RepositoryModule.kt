package com.mamsky.pcsprofile.data.di

import com.mamsky.pcsprofile.data.ProfileApiService
import com.mamsky.pcsprofile.data.ProfileRepository
import com.mamsky.pcsprofile.data.ProfileRepositoryImpl
import com.mamsky.pcsprofile.data.local.DbTransactionProvider
import com.mamsky.pcsprofile.data.local.ProfileDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProfileRepository(
        apiService: ProfileApiService,
        database: ProfileDatabase,
        transactionProvider: DbTransactionProvider
    )
    : ProfileRepository = ProfileRepositoryImpl(apiService, database, transactionProvider)

}