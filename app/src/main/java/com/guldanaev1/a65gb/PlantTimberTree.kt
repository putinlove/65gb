package com.guldanaev1.a65gb

import android.app.Application
import timber.log.Timber

class PlantTimberTree : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
