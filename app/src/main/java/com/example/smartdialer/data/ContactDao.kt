package com.example.smartdialer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<ContactEntity>)

    @Query("SELECT * FROM contacts ORDER BY isFavorite DESC, callCount DESC, name ASC")
    fun allContactsFlow(): Flow<List<ContactEntity>>

    @Query("""
        SELECT * FROM contacts 
        WHERE t9Name LIKE :t9Param 
           OR LOWER(name) LIKE LOWER(:nameParam)
           OR number LIKE :numParam
        ORDER BY isFavorite DESC, callCount DESC
    """)
    suspend fun search(t9Param: String, nameParam: String, numParam: String): List<ContactEntity>

    @Query("UPDATE contacts SET callCount = callCount + 1 WHERE number = :number")
    suspend fun incrementCallCount(number: String)
}
