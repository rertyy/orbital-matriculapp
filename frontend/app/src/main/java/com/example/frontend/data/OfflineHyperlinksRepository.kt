package com.example.frontend.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Hyperlink] from a given data source.
 */
class OfflineHyperlinksRepository(private val hyperlinkDao: HyperlinkDao) : HyperlinksRepository  {
    override fun getAllHyperlinksStream(): Flow<List<Hyperlink>> = hyperlinkDao.getAllHyperlinks()

    override fun getHyperlinkStream(id: Int): Flow<Hyperlink?> = hyperlinkDao.getHyperlink(id)

    override suspend fun insertHyperlink(Hyperlink: Hyperlink) = hyperlinkDao.insert(Hyperlink)

    override suspend fun deleteHyperlink(Hyperlink: Hyperlink) = hyperlinkDao.delete(Hyperlink)

    override suspend fun updateHyperlink(Hyperlink: Hyperlink) = hyperlinkDao.update(Hyperlink)
}