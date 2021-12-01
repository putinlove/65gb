package com.guldanaev1.a65gb

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract.*

private const val NUMBERS_SELECTION = CommonDataKinds.Phone.CONTACT_ID + " =?"
private const val EMAILS_SELECTION = CommonDataKinds.Email.CONTACT_ID + " =?"
private const val DESCRIPTION_SELECTION = Data.RAW_CONTACT_ID + "=?" +
        " AND " + Data.MIMETYPE + "='" +
        CommonDataKinds.Note.CONTENT_ITEM_TYPE + "'"
private const val BIRTHDAY_DATE_SELECTION = Data.CONTACT_ID + "=?" +
        " AND " + Data.MIMETYPE + " = '" +
        CommonDataKinds.Event.CONTENT_ITEM_TYPE +
        "' AND " + CommonDataKinds.Event.TYPE +
        " = " + CommonDataKinds.Event.TYPE_BIRTHDAY
private const val CONTACTS_SELECTION = Contacts._ID + " =?"

class ContactResolver(private val context: Context) {
    private fun getContactNumbers(id: String): List<String> {
        val numbers = mutableListOf<String>()
        val phoneUri = CommonDataKinds.Phone.CONTENT_URI
        context.contentResolver?.query(
            phoneUri,
            null,
            NUMBERS_SELECTION,
            arrayOf(id),
            null
        )?.use {
            if (it.moveToNext()) {
                val number = it.getString(it.getColumnIndexOrThrow(CommonDataKinds.Phone.NUMBER))
                if (number != null) {
                    numbers.add(number)
                }
            }
        }
        return numbers
    }

    private fun getContactEmails(id: String): List<String> {
        val emails = mutableListOf<String>()
        val emailUri = CommonDataKinds.Email.CONTENT_URI
        context.contentResolver?.query(
            emailUri,
            null,
            EMAILS_SELECTION,
            arrayOf(id),
            null
        )?.use {
            if (it.moveToNext()) {
                val email = it.getString(it.getColumnIndexOrThrow(CommonDataKinds.Phone.NUMBER))
                if (email != null) {
                    emails.add(email)
                }
            }
        }
        return emails
    }

    private fun getContactDescription(id: String): String {
        var note = "-"
        context.contentResolver?.query(
            Data.CONTENT_URI,
            arrayOf(CommonDataKinds.Note.NOTE),
            DESCRIPTION_SELECTION,
            arrayOf(id),
            null
        )?.use {
            if (it.moveToNext()) {
                if (it.getType(0) == Cursor.FIELD_TYPE_STRING) {
                    note = it.getString(0)
                }
            }
        }
        return note
    }

    private fun getContactBirthdayDate(id: String): String? {
        var birthday: String? = null
        context.contentResolver?.query(
            Data.CONTENT_URI,
            arrayOf(CommonDataKinds.Event.START_DATE),
            BIRTHDAY_DATE_SELECTION,
            arrayOf(id),
            null
        )?.use {
            if (it.moveToNext()) {
                birthday = it.getString(0)
            }
        }
        return birthday
    }

    fun getContactList(): List<ContactModel> {
        val contactList = mutableListOf<ContactModel>()
        val contactsUri = Contacts.CONTENT_URI
        context.contentResolver?.query(
            contactsUri,
            null,
            null,
            null,
            null
        )?.use {
            while (it.moveToNext()) {
                val id = it.getString(
                    it.getColumnIndexOrThrow(Contacts._ID)
                )
                val image = it.getString(
                    it.getColumnIndexOrThrow(Contacts.PHOTO_URI)
                )
                val name = it.getString(
                    it.getColumnIndexOrThrow(Contacts.DISPLAY_NAME)
                )
                val number = getContactNumbers(id)
                contactList.add(
                    ContactModel(
                        photoResource = image,
                        contactName = name,
                        numberList = number,
                        emailList = null,
                        birthday = null,
                        description = null,
                        id = id
                    )
                )
            }
        }
        return contactList
    }

    fun getContactDetails(id: String): ContactModel? {
        val contact: ContactModel? = null
        val contactsUri = Contacts.CONTENT_URI
        context.contentResolver?.query(
            contactsUri,
            null,
            CONTACTS_SELECTION,
            arrayOf(id),
            null
        )?.use {
            if (it.moveToNext()) {
                val image = it.getString(
                    it.getColumnIndexOrThrow(Contacts.PHOTO_URI)
                )
                val name = it.getString(
                    it.getColumnIndexOrThrow(Contacts.DISPLAY_NAME)
                )
                val numbers: List<String> = getContactNumbers(id)
                val emails: List<String> = getContactEmails(id)
                val birthday = getContactBirthdayDate(id)
                val note = getContactDescription(id)
                return ContactModel(
                    photoResource = image,
                    contactName = name,
                    numberList = numbers,
                    emailList = emails,
                    birthday = birthday,
                    description = note,
                    id = id
                )
            }
        }
        return contact
    }
}
