package com.guldanaev1.a65gb.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun plusContactListComponent(contactListModule: ContactListModule):ContactListComponent
    fun plusContactDetailsComponent(contactDetailsModule: ContactDetailsModule): ContactDetailsComponent
}
