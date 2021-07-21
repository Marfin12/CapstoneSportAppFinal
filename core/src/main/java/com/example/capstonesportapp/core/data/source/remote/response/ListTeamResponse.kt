package com.example.capstonesportapp.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListTeamResponse(
    @field:SerializedName("teams")
    val team: List<TeamResponse>
)
