package com.mamsky.pcsprofile.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM 'Profiles' WHERE id = :id")
    fun getById(id: String): Flow<ProfileEntity?>

    @Query("SELECT * FROM 'Profiles'")
    fun getByIds(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(profiles: List<ProfileEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ProfileEntity)

    @Query("DELETE FROM 'Profiles' WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM 'Profiles'")
    suspend fun deleteAll()
}