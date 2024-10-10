package com.inmobixpress.repository

import com.inmobixpress.dao.RequestTypeDao
import com.inmobixpress.dao.RequestTypeDaoImpl
import com.inmobixpress.model.RequestType

class RequestTypeRepository(
    private val dao: RequestTypeDao = RequestTypeDaoImpl
) {
    suspend fun getAll(): List<RequestType> = dao.getAll()
    suspend fun findById(id: Int): RequestType? = dao.findById(id)
    suspend fun insert(requestType: RequestType) = dao.insert(requestType)
    suspend fun update(id: Int, requestType: RequestType) = dao.update(id, requestType)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}