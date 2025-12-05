package com.example.smartdialer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocklist")
data class BlockListEntry(
    @PrimaryKey val phoneNumber: String,
    val note: String?,
    val blockedAt: Long = System.currentTimeMillis()
)
