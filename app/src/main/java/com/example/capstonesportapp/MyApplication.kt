package com.example.capstonesportapp

import android.app.Application
import com.example.capstonesportapp.core.di.databaseModule
import com.example.capstonesportapp.core.di.networkModule
import com.example.capstonesportapp.core.di.repositoryModule
import com.example.capstonesportapp.di.useCaseModule
import com.example.capstonesportapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.*
import org.koin.core.logger.Level

open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}
