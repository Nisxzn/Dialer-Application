package com.example.smartdialer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CallLogDao {

    @Insert
    suspend fun insert(entry: CallLogEntity)

    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<CallLogEntity>
}
