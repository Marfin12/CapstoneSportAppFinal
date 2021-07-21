package com.example.capstonesportapp.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @field:SerializedName("name_en")
    val name_en: String
)
