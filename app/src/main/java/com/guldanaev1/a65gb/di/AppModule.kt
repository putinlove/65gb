package com.guldanaev1.a65gb.di

import android.content.Context
import com.guldanaev1.a65gb.ContactRepository
import com.guldanaev1.a65gb.ContactResolver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun providesContext() = context

    @Singleton
    @Provides
    fun providesResolver(
        context: Context
    ) = ContactResolver(context)
}

@Module
class ContactListModule {

    @Provides
    @ContactListScope
    fun providesRepository(
        contactResolver: ContactResolver
    ) = ContactRepository(contactResolver)
}

@Module
class ContactDetailsModule {

    @Provides
    @ContactDetailsScope
    fun providesRepository(
        contactResolver: ContactResolver
    ) = ContactRepository(contactResolver)
}
