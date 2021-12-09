package com.guldanaev1.a65gb

import android.content.Context
import io.reactivex.rxjava3.core.Single

class ContactRepository(context: Context) {
    private val contactResolver = ContactResolver(context)
    fun loadContactList(query: String): Single<List<ContactModel>> =
        Single.fromCallable<List<ContactModel>> {
            Thread.sleep(1000)
            contactResolver.getContactList(query)
        }

    fun loadContactDetails(id: String): Single<ContactModel> =
        Single.fromCallable<ContactModel> {
            Thread.sleep(1000)
            contactResolver.getContactDetails(id)
        }
}
