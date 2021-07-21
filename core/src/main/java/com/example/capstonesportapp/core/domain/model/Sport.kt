package com.example.capstonesportapp.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sport(
    val sportsId: String,
    val name: String,
    val description: String,
    val category: String,
    val image: String
) : Parcelable