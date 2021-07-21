package com.example.capstonesportapp.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstonesportapp.core.data.Resource
import com.example.capstonesportapp.core.data.source.remote.network.ApiConfig
import com.example.capstonesportapp.core.domain.model.Country
import com.example.capstonesportapp.core.domain.model.Team
import com.example.capstonesportapp.core.domain.usecase.SportUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*


class HomeViewModel(private val sportUseCase: SportUseCase) : ViewModel() {
    val sportAdapter = sportUseCase.getAllSport().asLiveData()
    val countryAdapter = sportUseCase.getAllCountry().asLiveData()
    val teamAdapter = sportUseCase.getAllTeam().asLiveData()
    val favoriteTeam = sportUseCase.getFavoriteTeam().asLiveData()
    val seenTeam = sportUseCase.getSeenTeam().asLiveData()

    val searchTeamAdapter = ArrayList<LiveData<Resource<List<Team>>>>()
    val teamSportAdapter = ArrayList<LiveData<Resource<List<Team>>>>()

    fun generateSportAdapter(countries: List<Country>?) =
        countries?.map {
            teamSportAdapter.add(
                sportUseCase.getTeamFromCountry(it.name_en).asLiveData()
            )
        }

    fun initSearchAdapter() =
        searchTeamAdapter.add(
            sportUseCase.searchTeam("", arrayListOf("")).asLiveData()
        )

    fun generateSearchAdapter(teamName: String, localDataTeam: ArrayList<String>) =
        searchTeamAdapter.set(0,
            sportUseCase.searchTeam(teamName, localDataTeam).asLiveData()
        )

    @ExperimentalCoroutinesApi
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @ExperimentalCoroutinesApi
    @FlowPreview
    val searchResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            ApiConfig.provideApiService(sportUseCase.getHttpClient()).searchTeam(it).team
        }
        .asLiveData()

}
