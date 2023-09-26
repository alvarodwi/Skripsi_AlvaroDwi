package me.varoa.nongki.di

import me.varoa.nongki.data.work.SyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val coreModule =
    module {
        worker {
            SyncWorker(appContext = androidContext(), params = get(), repository = get(), client = get())
        }
    }
