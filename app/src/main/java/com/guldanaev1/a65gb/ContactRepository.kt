package com.guldanaev1.a65gb

import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ContactRepository @Inject constructor(private val contactResolver: ContactResolver) {
    fun loadContactList(query: String): Single<List<ContactModel>> =
        Single.fromCallable<List<ContactModel>> {
            Thread.sleep(100)
            contactResolver.getContactList(query)
        }

    fun loadContactDetails(id: String): Single<ContactModel> =
        Single.fromCallable<ContactModel> {
            Thread.sleep(100)
            contactResolver.getContactDetails(id)
        }
}
