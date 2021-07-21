package com.example.capstonesportapp.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name_en")
    var name_en: String,
)
