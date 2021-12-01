package com.guldanaev1.a65gb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val contactProviderRepository = ContactRepository(application)
    val contactList = contactProviderRepository.mutableContactList as LiveData<List<ContactModel>>

    fun requestContactList() {
        contactProviderRepository.loadContactList()
    }
}
