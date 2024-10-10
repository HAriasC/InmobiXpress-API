package com.inmobixpress.repository

import com.inmobixpress.dao.PublishingDao
import com.inmobixpress.dao.PublishingDaoImpl
import com.inmobixpress.model.Publishing

class PublishingRepository(
    private val dao: PublishingDao = PublishingDaoImpl
) {
    suspend fun getAll(): List<Publishing> = dao.getAll()
    suspend fun findById(id: Int): Publishing? = dao.findById(id)
    suspend fun insert(publishing: Publishing) = dao.insert(publishing)
    suspend fun update(id: Int, publishing: Publishing) = dao.update(id, publishing)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}