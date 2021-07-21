package com.example.capstonesportapp.core.data.source.remote.network

import com.example.capstonesportapp.core.data.source.remote.response.ListCountryResponse
import com.example.capstonesportapp.core.data.source.remote.response.ListSportResponse
import com.example.capstonesportapp.core.data.source.remote.response.ListTeamResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SportApiService {
    @GET("all_sports.php")
    suspend fun getSportList(): ListSportResponse

    @GET("all_countries.php")
    suspend fun getCountryList(): ListCountryResponse

    @GET("search_all_teams.php")
    suspend fun getTeamList(@Query("c") country: String, @Query("s") sport: String): ListTeamResponse

    @GET("searchteams.php")
    suspend fun searchTeam(@Query("t") team: String): ListTeamResponse
}
