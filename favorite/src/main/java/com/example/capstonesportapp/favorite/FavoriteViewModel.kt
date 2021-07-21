package com.example.capstonesportapp.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstonesportapp.core.domain.model.Team
import com.example.capstonesportapp.core.domain.usecase.SportUseCase

class FavoriteViewModel(private val sportUseCase: SportUseCase) : ViewModel() {
    val favoriteTeam = sportUseCase.getFavoriteTeam().asLiveData()

    fun removeFavoriteSport(team: Team) =
        sportUseCase.setFavoriteTeam(team, false)
}
