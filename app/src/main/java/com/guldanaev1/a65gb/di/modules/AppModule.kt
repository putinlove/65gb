package com.guldanaev1.a65gb.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContactRepositoryModule::class])
class AppModule(private val application: Application) {
    @Singleton
    @Provides
    fun provideContext(): Context = application
}
