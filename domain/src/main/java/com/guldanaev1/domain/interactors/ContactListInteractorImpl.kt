package com.guldanaev1.domain.interactors

class ContactListInteractorImpl(private val repository: ContactDataSource) :
    ContactListInteractor {
    override fun loadContacts(query: String) =
        repository.getContactList(query)
}
