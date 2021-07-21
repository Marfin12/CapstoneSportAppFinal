package com.example.capstonesportapp.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport")
data class SportEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "sportsId")
    var sportsId: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "image")
    var image: String
)
