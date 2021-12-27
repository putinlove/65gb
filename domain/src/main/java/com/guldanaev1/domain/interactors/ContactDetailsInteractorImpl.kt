package com.guldanaev1.domain.interactors

class ContactDetailsInteractorImpl(private val repository: ContactDataSource) :
    ContactDetailsInteractor {
    override fun loadContactDetails(id: String) =
        repository.getContactDetails(id)
}
