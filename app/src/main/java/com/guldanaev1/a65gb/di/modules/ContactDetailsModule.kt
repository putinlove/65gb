package com.guldanaev1.a65gb.di.modules


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guldanaev1.a65gb.di.keys.ViewModelKey
import com.guldanaev1.a65gb.di.scopes.ContactDetailsScope
import com.guldanaev1.domain.interactors.ContactDataSource
import com.guldanaev1.domain.interactors.ContactDetailsInteractor
import com.guldanaev1.domain.interactors.ContactDetailsInteractorImpl
import com.guldanaev1.presentation.di.factories.ViewModelFactory
import com.guldanaev1.presentation.viewmodels.ContactDetailsViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ContactDetailsModule {

    @Provides
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    fun provideContactDetailsViewModel(
        interactor: ContactDetailsInteractor
    ): ViewModel = ContactDetailsViewModel(interactor)

    @ContactDetailsScope
    @Provides
    fun provideDetailsInteractor(repository: ContactDataSource)
            : ContactDetailsInteractor =
        ContactDetailsInteractorImpl(repository)

    @ContactDetailsScope
    @Provides
    fun provideViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory = viewModelFactory
}
