/*
 * Created by Tamás Szincsák on 2018-11-02.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        App.instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
