package com.guldanaev1.domain.interactors

import com.guldanaev1.domain.entities.ContactModel

interface ContactDataSource {
    fun getContactList(query: String): List<ContactModel>
    fun getContactDetails(id: String): ContactModel?
}
