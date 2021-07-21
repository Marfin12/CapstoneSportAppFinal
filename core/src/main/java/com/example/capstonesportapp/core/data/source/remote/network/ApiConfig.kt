package com.example.capstonesportapp.core.data.source.remote.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun provideApiService(httpClient: OkHttpClient): SportApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.thesportsdb.com/api/v1/json/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
        return retrofit.create(SportApiService::class.java)
    }
}
