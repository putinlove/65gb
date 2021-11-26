package com.guldanaev1.a65gb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ContactDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val contactProviderRepository = ContactRepository(application)
    val contactDetails = contactProviderRepository.mutableContactDetails as LiveData<ContactModel>

    fun requestContactDetails(id: String) {
        contactProviderRepository.loadContactDetails(id)
    }
}