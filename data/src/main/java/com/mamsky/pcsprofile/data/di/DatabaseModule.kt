package com.mamsky.pcsprofile.data.di

import android.content.Context
import androidx.room.Room
import com.mamsky.pcsprofile.data.local.ProfileDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        ProfileDatabase::class.java,
        ProfileDatabase.DATABASE_NAME
    ).build()

}
