package com.example.frontend.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HyperlinkDao {

    @Query("SELECT * from Hyperlink ORDER BY name ASC")
    fun getAllHyperlinks(): Flow<List<Hyperlink>>

    @Query("SELECT * from Hyperlink WHERE id = :id")
    fun getHyperlink(id: Int): Flow<Hyperlink>

    @Upsert
    suspend fun insert(hyperlink: Hyperlink)

    @Update
    suspend fun update(hyperlink: Hyperlink)

    @Delete
    suspend fun delete(hyperlink: Hyperlink)
}