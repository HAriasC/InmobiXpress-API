package com.inmobixpress.repository

import com.inmobixpress.dao.RequestHasPublishingDao
import com.inmobixpress.dao.RequestHasPublishingDaoImpl
import com.inmobixpress.model.RequestHasPublishing

class RequestHasPublishingRepository(
    private val dao: RequestHasPublishingDao = RequestHasPublishingDaoImpl
) {
    suspend fun getAll(): List<RequestHasPublishing> = dao.getAll()
    suspend fun findById(requestId: Int, publishingId: Int): RequestHasPublishing? =
        dao.findById(requestId, publishingId)

    suspend fun insert(requestHasPublishing: RequestHasPublishing) =
        dao.insert(requestHasPublishing)

    suspend fun update(
        requestId: Int,
        publishingId: Int,
        requestHasPublishing: RequestHasPublishing
    ) = dao.update(requestId, publishingId, requestHasPublishing)

    suspend fun delete(requestId: Int, publishingId: Int): Boolean =
        dao.delete(requestId, publishingId)
}