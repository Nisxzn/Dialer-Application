package com.example.smartdialer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val number: String,
    val t9Name: String,
    val callCount: Int = 0,
    val isFavorite: Boolean = false
)
