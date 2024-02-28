package me.varoa.nongki.di

import android.content.Context
import androidx.room.Room
import me.varoa.nongki.data.local.AppDatabase
import me.varoa.nongki.data.prefs.DataStoreManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule =
    module {
        fun provideDatabase(context: Context): AppDatabase =
            Room
                .databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "nongki.db",
                ).build()

        single { provideDatabase(androidContext()) }

        single {
            val database = get<AppDatabase>()
            database.searchDao
        }

        single {
            val database = get<AppDatabase>()
            database.hangoutDao
        }

        single {
            DataStoreManager(androidContext())
        }
    }
