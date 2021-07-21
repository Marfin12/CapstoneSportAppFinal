package com.example.capstonesportapp.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.capstonesportapp.core.data.source.local.entity.CountryEntity
import com.example.capstonesportapp.core.data.source.local.entity.SportEntity
import com.example.capstonesportapp.core.data.source.local.entity.TeamEntity

@Database(entities = [SportEntity::class, TeamEntity::class, CountryEntity::class], version = 3, exportSchema = false)
abstract class SportDatabase : RoomDatabase() {
    abstract fun sportDao(): SportDao
}