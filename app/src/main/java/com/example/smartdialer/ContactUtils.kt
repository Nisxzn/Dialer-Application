package com.example.smartdialer

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract

fun loadAllContacts(context: Context): List<Contact> {
    val list = mutableListOf<Contact>()
    val resolver: ContentResolver = context.contentResolver

    val cursor = resolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            val name = it.getString(nameIdx) ?: continue
            val number = it.getString(numIdx) ?: continue
            list.add(Contact(name, number))
        }
    }

    return list
}
