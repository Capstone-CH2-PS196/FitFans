package com.capstonech2.fitfans

import android.app.Application
import androidx.room.Room
import com.capstonech2.fitfans.BuildConfig.DEBUG
import com.capstonech2.fitfans.data.GymRepository
import com.capstonech2.fitfans.data.NoteRepository
import com.capstonech2.fitfans.data.local.FitfansDatabase
import com.capstonech2.fitfans.data.remote.service.ApiService
import com.capstonech2.fitfans.ui.auth.basicinformation.BasicInformationViewModel
import com.capstonech2.fitfans.ui.auth.login.LoginViewModel
import com.capstonech2.fitfans.ui.home.HomeViewModel
import com.capstonech2.fitfans.ui.note.NoteViewModel
import com.capstonech2.fitfans.ui.profile.ProfileViewModel
import com.capstonech2.fitfans.utils.DB_NAME
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }

    companion object {
        private val loggingInterceptor = HttpLoggingInterceptor().setLevel(if (DEBUG) BODY else NONE)
        private val converter = GsonConverterFactory.create()
        private val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val appModule = module {
            single {
                Retrofit.Builder().baseUrl(BuildConfig.API_URL).addConverterFactory(converter).client(client)
                    .build().create(ApiService::class.java)
            }
            single {
                Room.databaseBuilder(get(), FitfansDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration().build()
            }
            single { GymRepository(get()) }
            single { NoteRepository(get()) }

            viewModel { HomeViewModel(get()) }
            viewModel { NoteViewModel(get()) }
            viewModel { BasicInformationViewModel(get()) }
            viewModel { LoginViewModel(get()) }
            viewModel { ProfileViewModel(get())}
        }
    }
}