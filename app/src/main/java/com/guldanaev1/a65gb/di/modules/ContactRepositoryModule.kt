package com.guldanaev1.a65gb.di.modules

import android.content.Context
import com.guldanaev1.domain.interactors.ContactDataSource
import com.guldanaev1.presentation.repositories.ContactDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContactRepositoryModule {
    @Provides
    @Singleton
    fun provideListRepository(
        context: Context
    ): ContactDataSource = ContactDataSourceImpl(context)
}
