package com.guldanaev1.a65gb.di

import com.guldanaev1.a65gb.ContactListFragment
import dagger.Subcomponent

@ContactListScope
@Subcomponent(modules = [ContactListModule::class])
interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}
