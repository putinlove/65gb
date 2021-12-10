package com.guldanaev1.a65gb.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guldanaev1.a65gb.ContactDetailsViewModel
import com.guldanaev1.a65gb.ContactListViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)
    abstract fun bindContactListViewModel(
        contactListViewModel: ContactListViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindContactDetailsViewModel(
        contactDetailsViewModel: ContactDetailsViewModel
    ): ViewModel
}
