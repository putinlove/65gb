package com.guldanaev1.a65gb.di.components

import com.guldanaev1.a65gb.di.modules.ContactListModule
import com.guldanaev1.a65gb.di.scopes.ContactListScope
import com.guldanaev1.presentation.di.ContactListFragmentComponent
import dagger.Subcomponent

@ContactListScope
@Subcomponent(modules = [ContactListModule::class])
interface ContactListComponent : ContactListFragmentComponent
