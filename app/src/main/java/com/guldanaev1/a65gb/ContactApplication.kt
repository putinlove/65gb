package com.guldanaev1.a65gb

import android.app.Application
import com.guldanaev1.a65gb.di.components.DaggerAppComponent
import com.guldanaev1.a65gb.di.modules.AppModule
import com.guldanaev1.presentation.BuildConfig
import com.guldanaev1.presentation.di.AppComponentOwner
import com.guldanaev1.presentation.di.AppContainer
import timber.log.Timber

class ContactApplication : Application(), AppContainer {
    override lateinit var appComponent: AppComponentOwner
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
