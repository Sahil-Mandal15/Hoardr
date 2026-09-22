package com.sahilarious.hoardr.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LinkEntity::class],
    version = 2,
    exportSchema = true
)
abstract class HoardrDatabase : RoomDatabase() {
    abstract fun hoardrDao() : HoardrDao
}