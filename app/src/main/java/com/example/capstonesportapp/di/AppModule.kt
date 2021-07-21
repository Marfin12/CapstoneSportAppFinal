package com.example.capstonesportapp.di

import com.example.capstonesportapp.core.domain.usecase.SportInteractor
import com.example.capstonesportapp.core.domain.usecase.SportUseCase
import com.example.capstonesportapp.detail.DetailViewModel
import com.example.capstonesportapp.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<SportUseCase> { SportInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
