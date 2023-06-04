package com.example.frontend.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Hyperlink] from a given data source.
 */
interface HyperlinksRepository {
    /**
     * Retrieve all the Hyperlinks from the the given data source.
     */
    fun getAllHyperlinksStream(): Flow<List<Hyperlink>>

    /**
     * Retrieve an Hyperlink from the given data source that matches with the [id].
     */
    fun getHyperlinkStream(id: Int): Flow<Hyperlink?>

    /**
     * Insert Hyperlink in the data source
     */
    suspend fun insertHyperlink(Hyperlink: Hyperlink)

    /**
     * Delete Hyperlink from the data source
     */
    suspend fun deleteHyperlink(Hyperlink: Hyperlink)

    /**
     * Update Hyperlink in the data source
     */
    suspend fun updateHyperlink(Hyperlink: Hyperlink)
}
