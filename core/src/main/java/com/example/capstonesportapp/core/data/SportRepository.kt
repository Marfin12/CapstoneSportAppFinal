package com.example.capstonesportapp.core.data

import com.example.capstonesportapp.core.data.source.local.LocalDataSource
import com.example.capstonesportapp.core.data.source.remote.RemoteDataSource
import com.example.capstonesportapp.core.data.source.remote.network.ApiResponse
import com.example.capstonesportapp.core.data.source.remote.response.CountryResponse
import com.example.capstonesportapp.core.data.source.remote.response.SportResponse
import com.example.capstonesportapp.core.data.source.remote.response.TeamResponse
import com.example.capstonesportapp.core.domain.model.Country
import com.example.capstonesportapp.core.domain.model.Sport
import com.example.capstonesportapp.core.domain.model.Team
import com.example.capstonesportapp.core.domain.repository.ISportRepository
import com.example.capstonesportapp.core.utils.AppExecutors
import com.example.capstonesportapp.core.utils.DataMapper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient

class SportRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val okHttpClient: OkHttpClient
) : ISportRepository {

    override fun getAllSport(): Flow<Resource<List<Sport>>> =
        object : NetworkBoundResource<List<Sport>, List<SportResponse>>() {
            override fun loadFromDB(): Flow<List<Sport>> {
                return localDataSource.getAllSport().map { DataMapper.mapSportEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Sport>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<SportResponse>>> =
                remoteDataSource.getAllSport()

            override suspend fun saveCallResult(data: List<SportResponse>) {
                val sportList = DataMapper.mapResponsesToSportEntities(data)
                localDataSource.insertSport(sportList)
            }
        }.asFlow()

    @FlowPreview
    override fun getAllTeam(): Flow<Resource<List<Team>>> =
        object : NetworkBoundResource<List<Team>, List<TeamResponse>>() {
            override fun loadFromDB(): Flow<List<Team>> {
                return localDataSource.getAllTeam().map { DataMapper.mapTeamEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Team>?): Boolean = false

            override suspend fun createCall(): Flow<ApiResponse<List<TeamResponse>>> =
                remoteDataSource.getTeamFromCountry("")

            override suspend fun saveCallResult(data: List<TeamResponse>) {
                val teamList = DataMapper.mapResponsesToTeamEntities(data)
                localDataSource.insertTeam(teamList)
            }
        }.asFlow()

    override fun getAllCountry(): Flow<Resource<List<Country>>> =
        object : NetworkBoundResource<List<Country>, List<CountryResponse>>() {
            override fun loadFromDB(): Flow<List<Country>> {
                return localDataSource.getAllCountry()
                    .map { DataMapper.mapCountryEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Country>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<CountryResponse>>> =
                remoteDataSource.getAllCountry()

            override suspend fun saveCallResult(data: List<CountryResponse>) {
                val countryList = DataMapper.mapResponsesToCountryEntities(data)
                localDataSource.insertCountry(countryList)
            }
        }.asFlow()

    override fun getTeamFromCountry(countryName: String): Flow<Resource<List<Team>>> =
        object : NetworkBoundResource<List<Team>, List<TeamResponse>>() {
            override fun loadFromDB(): Flow<List<Team>> {
                return localDataSource.getTeamFromCountry(countryName)
                    .map { DataMapper.mapTeamEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Team>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<TeamResponse>>> =
                remoteDataSource.getTeamFromCountry(countryName)

            override suspend fun saveCallResult(data: List<TeamResponse>) {
                val teamList = DataMapper.mapResponsesToTeamEntities(data)
                localDataSource.insertTeam(teamList)
            }
        }.asFlow()

    override fun searchTeam(teamName: String, localDataTeam: ArrayList<String>): Flow<Resource<List<Team>>> =
        object : NetworkBoundResource<List<Team>, List<TeamResponse>>() {
            override fun loadFromDB(): Flow<List<Team>> {
                return localDataSource.searchTeam(teamName)
                    .map { DataMapper.mapTeamEntitiesToDomain(it) }
            }

            override fun shouldFetch(data: List<Team>?): Boolean = true


            override suspend fun createCall(): Flow<ApiResponse<List<TeamResponse>>> =
                remoteDataSource.searchTeam(teamName, localDataTeam)

            override suspend fun saveCallResult(data: List<TeamResponse>) {
                val teamList = DataMapper.mapResponsesToTeamEntities(data)
                localDataSource.insertTeam(teamList)
            }
        }.asFlow()


    override fun getFavoriteTeam(): Flow<List<Team>> {
        return localDataSource.getFavoriteTeam().map { DataMapper.mapTeamEntitiesToDomain(it) }
    }

    override fun getSeenTeam(): Flow<List<Team>> {
        return localDataSource.getSeenTeam().map { DataMapper.mapTeamEntitiesToDomain(it) }
    }

    override fun setFavoriteTeam(team: Team, state: Boolean) {
        val teamEntity = DataMapper.mapDomainToTeamEntity(team)
        appExecutors.diskIO().execute { localDataSource.setFavoriteTeam(teamEntity, state) }
    }

    override fun setSeenTeam(team: Team, state: Boolean) {
        val teamEntity = DataMapper.mapDomainToTeamEntity(team)
        appExecutors.diskIO().execute { localDataSource.setSeenTeam(teamEntity, state) }
    }

    override fun getHttpClient(): OkHttpClient = okHttpClient
}
