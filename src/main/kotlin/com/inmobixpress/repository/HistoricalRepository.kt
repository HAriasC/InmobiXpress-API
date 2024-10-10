package com.inmobixpress.repository

import com.inmobixpress.dao.HistoricalDao
import com.inmobixpress.dao.HistoricalDaoImpl
import com.inmobixpress.model.Historical

class HistoricalRepository(
    private val dao: HistoricalDao = HistoricalDaoImpl
) {
    suspend fun getAll(): List<Historical> = dao.getAll()
    suspend fun findById(publishingStateId: Int, publishingId: Int): Historical? =
        dao.findById(publishingStateId, publishingId)

    suspend fun insert(historical: Historical) = dao.insert(historical)
    suspend fun update(publishingStateId: Int, publishingId: Int, historical: Historical) =
        dao.update(publishingStateId, publishingId, historical)

    suspend fun delete(publishingStateId: Int, publishingId: Int): Boolean =
        dao.delete(publishingStateId, publishingId)
}