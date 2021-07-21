package com.example.capstonesportapp.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SportResponse(
    @field:SerializedName("idSport")
    val sportsId: String,

    @field:SerializedName("strSport")
    val name: String,

    @field:SerializedName("strSportDescription")
    val description: String,

    @field:SerializedName("strFormat")
    val category: String,

    @field:SerializedName("strSportThumb")
    val image: String
)
