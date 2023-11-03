package cz.ackee.testtask

import android.app.Application
import cz.ackee.testtask.characters.di.characterModule
import cz.ackee.testtask.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(coreModule, characterModule)
        }
    }
}
