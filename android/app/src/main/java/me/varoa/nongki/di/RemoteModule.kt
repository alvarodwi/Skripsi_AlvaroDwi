package me.varoa.nongki.di

import android.content.Context
import coil.ImageLoader
import logcat.logcat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val remoteModule =
    module {
        val defaultTimeout = 5L

        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor { message ->
                        logcat("API") { message }
                    }.setLevel(HttpLoggingInterceptor.Level.BASIC),
                )
                .connectTimeout(defaultTimeout, TimeUnit.MINUTES)
                .readTimeout(defaultTimeout, TimeUnit.MINUTES)
                .build()
        }

        single { provideOkHttpClient() }

        fun provideCoilLoader(
            context: Context,
            client: OkHttpClient,
        ) = ImageLoader.Builder(context)
            .okHttpClient(client)
            .crossfade(true)
            .build()

        single { provideCoilLoader(androidContext(), get()) }
    }
