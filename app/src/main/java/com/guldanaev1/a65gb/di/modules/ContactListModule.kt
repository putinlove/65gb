package com.guldanaev1.a65gb.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guldanaev1.a65gb.di.keys.ViewModelKey
import com.guldanaev1.a65gb.di.scopes.ContactListScope
import com.guldanaev1.domain.interactors.ContactDataSource
import com.guldanaev1.domain.interactors.ContactListInteractor
import com.guldanaev1.domain.interactors.ContactListInteractorImpl
import com.guldanaev1.presentation.di.factories.ViewModelFactory
import com.guldanaev1.presentation.viewmodels.ContactListViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ContactListModule {
    @Provides
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)

    fun provideContactListViewModel(
        interactor: ContactListInteractor
    ): ViewModel = ContactListViewModel(interactor)

    @ContactListScope
    @Provides
    fun provideListInteractor(repository: ContactDataSource)
            : ContactListInteractor =
        ContactListInteractorImpl(repository)

    @ContactListScope
    @Provides
    fun provideViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory = viewModelFactory
}
