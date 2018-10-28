package hu.stepintomeetups.gitstar

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        App.instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
