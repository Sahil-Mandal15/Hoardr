package com.sahilarious.hoardr.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HoardrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(linkEntity: LinkEntity): Long

    @Delete
    suspend fun deleteLinkById(entity: LinkEntity)

    @Query("SELECT * FROM LinkEntity")
    fun getAllLinks(): Flow<List<LinkEntity>>

    @Query("SELECT * FROM LinkEntity WHERE isRead = 0 OR LOWER(category) != 'article'")
    fun getUnreadLinks(): Flow<List<LinkEntity>>

    @Query("UPDATE LinkEntity SET isRead = :isRead WHERE id = :id")
    suspend fun updateReadStatus(id: Long, isRead: Boolean)
}