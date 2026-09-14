package com.sahilarious.hoardr.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HoardrDao {
    @Insert
    suspend fun insertLink(linkEntity: LinkEntity)

    @Delete
    suspend fun deleteLinkById(entity: LinkEntity)

    @Query("SELECT * FROM LinkEntity")
    suspend fun getAllLinks(): List<LinkEntity>
}