package com.guldanaev1.a65gb.di.components

import com.guldanaev1.a65gb.di.modules.AppModule
import com.guldanaev1.presentation.di.AppComponentOwner
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : AppComponentOwner {
    override fun plusContactListComponent(): ContactListComponent
    override fun plusContactDetailsComponent(): ContactDetailsComponent
}
