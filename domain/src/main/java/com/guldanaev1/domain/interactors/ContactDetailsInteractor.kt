package com.guldanaev1.domain.interactors

import com.guldanaev1.domain.entities.ContactModel

interface ContactDetailsInteractor {
    fun loadContactDetails(id: String): ContactModel?
}
