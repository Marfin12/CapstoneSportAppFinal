package com.example.capstonesportapp.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "teamId")
    var teamId: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "sportCategory")
    var sportCategory: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "country")
    var country: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,

    @ColumnInfo(name = "isSeen")
    var isSeen: Boolean = false,

    @ColumnInfo(name = "locaton")
    var location: String,
)
