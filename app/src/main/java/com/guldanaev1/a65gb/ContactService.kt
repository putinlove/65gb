package com.guldanaev1.a65gb

import android.app.Service
import android.content.Intent
import android.os.Binder
import java.lang.ref.WeakReference
import kotlin.concurrent.thread

class ContactService : Service() {

    private val binder = ContactBinder()

    override fun onBind(intent: Intent) = binder

    fun getContactList(callback: ContactListLoadListener) {
        val weakReference = WeakReference(callback)
        thread(start = true) {
            Thread.sleep(1000)
            weakReference.get()?.onContactListLoaded(ContactResolver.getContactList(this))
        }
    }

    fun getContactDetails(id: String, callback: ContactDetailsLoadListener) {
        val weakReference = WeakReference(callback)
        thread(start = true) {
            Thread.sleep(1000)
            weakReference.get()?.onContactDetailsLoaded(ContactResolver.getContactDetails(this, id))
        }
    }

    inner class ContactBinder : Binder() {
        fun getService() = this@ContactService
    }
}
