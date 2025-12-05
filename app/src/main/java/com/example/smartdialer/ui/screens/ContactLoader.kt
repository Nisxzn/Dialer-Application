package com.example.smartdialer.ui.screens

import android.content.Context
import android.provider.ContactsContract
import com.example.smartdialer.Contact

fun loadAllContacts(context: Context): List<Contact> {
    val list = mutableListOf<Contact>()
    val resolver = context.contentResolver
    val cursor = resolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null, null, null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            val name = it.getString(nameIndex) ?: continue
            val number = it.getString(numberIndex) ?: continue
            list.add(Contact(name, number))
        }
    }

    return list
}
