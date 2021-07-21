package com.example.capstonesportapp.core.utils

import com.example.capstonesportapp.core.data.source.local.entity.CountryEntity
import com.example.capstonesportapp.core.data.source.local.entity.SportEntity
import com.example.capstonesportapp.core.data.source.local.entity.TeamEntity
import com.example.capstonesportapp.core.data.source.remote.response.CountryResponse
import com.example.capstonesportapp.core.data.source.remote.response.SportResponse
import com.example.capstonesportapp.core.data.source.remote.response.TeamResponse
import com.example.capstonesportapp.core.domain.model.Country
import com.example.capstonesportapp.core.domain.model.Sport
import com.example.capstonesportapp.core.domain.model.Team

object DataMapper {
    fun mapResponsesToSportEntities(input: List<SportResponse>): List<SportEntity> {
        val sportList = ArrayList<SportEntity>()
        input.map {
            val sport = SportEntity(
                sportsId = it.sportsId,
                name = it.name,
                description = it.description,
                category = it.category,
                image = it.image
            )
            sportList.add(sport)
        }

        return sportList
    }

    fun mapResponsesToTeamEntities(input: List<TeamResponse>): List<TeamEntity> {
        val teamList = ArrayList<TeamEntity>()
        input.map {
            val team = TeamEntity(
                teamId = it.teamId,
                name = it.name,
                description = it.description,
                sportCategory = it.sportCategory,
                country = it.country,
                image = it.image,
                location = it.location
            )
            teamList.add(team)
        }

        return teamList
    }

    fun mapResponsesToCountryEntities(input: List<CountryResponse>): List<CountryEntity> {
        val countryList = ArrayList<CountryEntity>()
        input.map {
            val team = CountryEntity(
                name_en = it.name_en
            )
            countryList.add(team)
        }

        return countryList
    }

    fun mapSportEntitiesToDomain(input: List<SportEntity>): List<Sport> =
        input.map {
            Sport(
                sportsId = it.sportsId,
                name = it.name,
                description = it.description,
                category = it.category,
                image = it.image
            )
        }

    fun mapTeamEntitiesToDomain(input: List<TeamEntity>): List<Team> =
        input.map {
            Team(
                teamId = it.teamId,
                name = it.name,
                description = it.description,
                sportCategory = it.sportCategory,
                country = it.country,
                image = it.image,
                isFavorite = it.isFavorite,
                isSeen = it.isSeen,
                location = it.location
            )
        }

    fun mapCountryEntitiesToDomain(input: List<CountryEntity>): List<Country> =
        input.map {
            Country(
                name_en = it.name_en
            )
        }

    fun mapDomainToTeamEntity(input: Team) = TeamEntity(
        teamId = input.teamId,
        name = input.name,
        description = input.description,
        sportCategory = input.sportCategory,
        country = input.country,
        image = input.image,
        isFavorite = input.isFavorite,
        isSeen = input.isSeen,
        location = input.location
    )
}