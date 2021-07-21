package com.example.capstonesportapp.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TeamResponse(
    @field:SerializedName("idTeam")
    val teamId: String,

    @field:SerializedName("strTeam")
    val name: String,

    @field:SerializedName("strDescriptionEN")
    var description: String,

    @field:SerializedName("strSport")
    val sportCategory: String,

    @field:SerializedName("strCountry")
    val country: String,

    @field:SerializedName("strTeamBadge")
    val image: String,

    @field:SerializedName("strStadiumLocation")
    val location: String
)
