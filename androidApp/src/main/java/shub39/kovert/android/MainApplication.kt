package shub39.kovert.android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import shub39.kovert.core.di.initKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MainApplication)
        }
    }
}