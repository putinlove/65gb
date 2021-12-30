package com.guldanaev1.domain.interactors

import com.guldanaev1.domain.entities.ContactModel

interface ContactListInteractor {
    fun loadContacts(query: String): List<ContactModel>
}
