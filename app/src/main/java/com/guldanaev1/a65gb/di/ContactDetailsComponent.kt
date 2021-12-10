package com.guldanaev1.a65gb.di

import com.guldanaev1.a65gb.ContactDetailsFragment
import dagger.Subcomponent

@ContactDetailsScope
@Subcomponent(modules = [ContactDetailsModule::class])
interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}
