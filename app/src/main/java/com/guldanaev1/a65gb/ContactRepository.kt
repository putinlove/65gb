package com.guldanaev1.a65gb

import android.content.Context
import androidx.lifecycle.MutableLiveData
import kotlin.concurrent.thread

class ContactRepository(context: Context) {
    private val contactResolver = ContactResolver(context)
    val mutableContactList = MutableLiveData<List<ContactModel>>()
    fun loadContactList(query: String) {
        thread(start = true) {
            mutableContactList.postValue(contactResolver.getContactList(query))
        }
    }

    val mutableContactDetails = MutableLiveData<ContactModel>()
    fun loadContactDetails(id: String) {
        thread(start = true) {
            mutableContactDetails.postValue(contactResolver.getContactDetails(id))
        }
    }
}
