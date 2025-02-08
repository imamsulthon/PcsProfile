package com.mamsky.pcsprofile.domain.di

import com.mamsky.pcsprofile.domain.usecase.FromCacheUseCase
import com.mamsky.pcsprofile.domain.usecase.SimpleUseCase
import com.mamsky.pcsprofile.domain.usecase.SimpleUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    @ViewModelScoped
    @RemoteSource
    fun provideBasicUseCase(simpleUseCase: SimpleUseCaseImpl): SimpleUseCase

    @Binds
    @ViewModelScoped
    @CacheSource
    fun provideCacheUseCase(cacheUseCase: FromCacheUseCase): SimpleUseCase

}

@Qualifier
annotation class RemoteSource

@Qualifier
annotation class CacheSource