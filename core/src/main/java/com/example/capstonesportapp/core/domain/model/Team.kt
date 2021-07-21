package com.example.capstonesportapp.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    val teamId: String,
    val name: String,
    var description: String,
    val sportCategory: String,
    val image: String,
    val country: String,
    val location: String,
    val isFavorite: Boolean,
    val isSeen: Boolean
) : Parcelable