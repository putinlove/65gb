package com.guldanaev1.a65gb.di.components

import com.guldanaev1.a65gb.di.modules.ContactDetailsModule
import com.guldanaev1.a65gb.di.scopes.ContactDetailsScope
import com.guldanaev1.presentation.di.ContactDetailsFragmentComponent
import dagger.Subcomponent

@ContactDetailsScope
@Subcomponent(modules = [ContactDetailsModule::class])
interface ContactDetailsComponent : ContactDetailsFragmentComponent
