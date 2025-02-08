package com.mamsky.pcsprofile.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("createdAt")
    val createdAt: String,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("avatar")
    val avatar: String,
    @ColumnInfo("city")
    val city: String,
    @ColumnInfo("country")
    val country: String,
    @ColumnInfo("county")
    val county: String,
    @ColumnInfo("address_no")
    val addressNo: String,
    @ColumnInfo("street")
    val street: String,
    @ColumnInfo("zip_code")
    val zipCode: String,
)
