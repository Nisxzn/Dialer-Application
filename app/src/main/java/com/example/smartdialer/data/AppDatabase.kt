package com.example.smartdialer.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ContactEntity::class,
        CallLogEntity::class,
        BlockListEntry::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao
    abstract fun callLogDao(): CallLogDao
    abstract fun blockListDao(): BlockListDao
}
