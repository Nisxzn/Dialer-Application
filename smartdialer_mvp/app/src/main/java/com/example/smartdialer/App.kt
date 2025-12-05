package com.example.smartdialer

import android.app.Application
import androidx.room.Room
import com.example.smartdialer.data.AppDatabase

class App : Application() {
    companion object {
        lateinit var db: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "smartdialer.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
