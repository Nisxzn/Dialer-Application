package com.example.smartdialer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_logs")
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val number: String,
    val timestamp: Long,
    val type: String // "INCOMING", "OUTGOING", "MISSED"
)
