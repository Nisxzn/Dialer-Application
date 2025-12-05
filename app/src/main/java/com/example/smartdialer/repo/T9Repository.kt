package com.example.smartdialer.repo

import com.example.smartdialer.data.AppDatabase
import com.example.smartdialer.data.ContactEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class T9Repository(private val db: AppDatabase) {

    private val dao = db.contactDao()

    suspend fun searchByT9OrText(t9Digits: String, text: String, number: String): List<ContactEntity> {
        val t9Param = "%$t9Digits%"
        val nameParam = "%$text%"
        val numParam = "%$number%"
        return withContext(Dispatchers.IO) {
            dao.search(t9Param, nameParam, numParam)
        }
    }

    suspend fun insertContacts(contacts: List<ContactEntity>) {
        withContext(Dispatchers.IO) {
            dao.insertAll(contacts)
        }
    }

    suspend fun incrementCallCount(number: String) {
        withContext(Dispatchers.IO) {
            dao.incrementCallCount(number)
        }
    }
}
