package com.inmobixpress.repository

import com.inmobixpress.dao.PublishingStateDao
import com.inmobixpress.dao.PublishingStateDaoImpl
import com.inmobixpress.model.PublishingState

class PublishingStateRepository(
    private val dao: PublishingStateDao = PublishingStateDaoImpl
) {
    suspend fun getAll(): List<PublishingState> = dao.getAll()
    suspend fun findById(id: Int): PublishingState? = dao.findById(id)
    suspend fun insert(publishingState: PublishingState) = dao.insert(publishingState)
    suspend fun update(id: Int, publishingState: PublishingState) = dao.update(id, publishingState)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}