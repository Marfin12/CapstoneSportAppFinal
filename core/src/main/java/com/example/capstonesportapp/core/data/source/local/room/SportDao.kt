package com.example.capstonesportapp.core.data.source.local.room


import androidx.room.*
import com.example.capstonesportapp.core.data.source.local.entity.CountryEntity
import com.example.capstonesportapp.core.data.source.local.entity.SportEntity
import com.example.capstonesportapp.core.data.source.local.entity.TeamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SportDao {

    @Query("SELECT * FROM sport")
    fun getAllSport(): Flow<List<SportEntity>>

    @Query("SELECT * FROM team")
    fun getAllTeam(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM team WHERE country = :countryName")
    fun getTeamFromCountry(countryName: String): Flow<List<TeamEntity>>

    @Query("SELECT * FROM country")
    fun getAllCountry(): Flow<List<CountryEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSport(sport: List<SportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: List<TeamEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: List<CountryEntity>)


    @Query("SELECT * FROM team where isFavorite = 1")
    fun getFavoriteTeam(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM team where isSeen = 1")
    fun getSeenTeam(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM team where name LIKE '%' || :teamName || '%'")
    fun searchTeam(teamName: String): Flow<List<TeamEntity>>

    @Update
    fun updateTeam(team: TeamEntity)
}
