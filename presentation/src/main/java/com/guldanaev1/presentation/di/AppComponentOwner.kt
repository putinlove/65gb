package com.guldanaev1.presentation.di

interface AppComponentOwner {
    fun plusContactListComponent(): ContactListFragmentComponent
    fun plusContactDetailsComponent(): ContactDetailsFragmentComponent
}
