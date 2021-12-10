package com.guldanaev1.a65gb

import android.app.Application
import com.guldanaev1.a65gb.di.AppComponent
import com.guldanaev1.a65gb.di.AppModule
import com.guldanaev1.a65gb.di.DaggerAppComponent
import timber.log.Timber

class ContactApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}
