package com.example.capstonesportapp.core.di

import androidx.room.Room
import com.example.capstonesportapp.core.data.SportRepository
import com.example.capstonesportapp.core.data.source.local.LocalDataSource
import com.example.capstonesportapp.core.data.source.local.room.SportDatabase
import com.example.capstonesportapp.core.data.source.remote.RemoteDataSource
import com.example.capstonesportapp.core.data.source.remote.network.ApiConfig
import com.example.capstonesportapp.core.domain.repository.ISportRepository
import com.example.capstonesportapp.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<SportDatabase>().sportDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("dicoding".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            SportDatabase::class.java, "Sport.db"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "thesportsdb.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/Yl6IYMBnxrwtn9DWo32vSutvY3gRn56iEfWj5/66c88=")
            .build()
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        ApiConfig.provideApiService(get())
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<ISportRepository> { SportRepository(get(), get(), get(), get()) }
}
