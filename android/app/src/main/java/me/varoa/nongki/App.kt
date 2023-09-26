package me.varoa.nongki

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import me.varoa.nongki.di.appModule
import me.varoa.nongki.di.coreModule
import me.varoa.nongki.di.localModule
import me.varoa.nongki.di.remoteModule
import me.varoa.nongki.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)

        startKoin {
            androidLogger()
            androidContext(this@App)
            workManagerFactory()
            // modules
            modules(
                listOf(
                    localModule,
                    remoteModule,
                    repositoryModule,
                    coreModule,
                    appModule,
                ),
            )
        }
    }
}
