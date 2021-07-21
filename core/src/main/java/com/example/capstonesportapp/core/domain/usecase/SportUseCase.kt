package com.example.capstonesportapp.core.domain.usecase

import com.example.capstonesportapp.core.data.Resource
import com.example.capstonesportapp.core.domain.model.Country
import com.example.capstonesportapp.core.domain.model.Sport
import com.example.capstonesportapp.core.domain.model.Team
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient

interface SportUseCase {
    fun getAllSport(): Flow<Resource<List<Sport>>>
    fun getAllTeam(): Flow<Resource<List<Team>>>
    fun getTeamFromCountry(countryName: String): Flow<Resource<List<Team>>>
    fun getAllCountry(): Flow<Resource<List<Country>>>

    fun getFavoriteTeam(): Flow<List<Team>>
    fun getSeenTeam(): Flow<List<Team>>

    fun setFavoriteTeam(team: Team, state: Boolean)
    fun setSeenTeam(team: Team, state: Boolean)
    fun searchTeam(teamName: String, localDataTeam: ArrayList<String>): Flow<Resource<List<Team>>>

    fun getHttpClient(): OkHttpClient
}
