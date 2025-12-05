package com.example.smartdialer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(entry: BlockListEntry)

    @Query("SELECT phoneNumber FROM blocklist")
    fun blockedNumbersFlow(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM blocklist WHERE phoneNumber = :num)")
    suspend fun isBlocked(num: String): Boolean

    @Query("DELETE FROM blocklist WHERE phoneNumber = :num")
    suspend fun remove(num: String)
}
