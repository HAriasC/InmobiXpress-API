package com.inmobixpress.repository

import com.inmobixpress.dao.RequestStateDao
import com.inmobixpress.dao.RequestStateDaoImpl
import com.inmobixpress.model.RequestState

class RequestStateRepository(
    private val dao: RequestStateDao = RequestStateDaoImpl
) {
    suspend fun getAll(): List<RequestState> = dao.getAll()
    suspend fun findById(id: Int): RequestState? = dao.findById(id)
    suspend fun insert(requestState: RequestState) = dao.insert(requestState)
    suspend fun update(id: Int, requestState: RequestState) = dao.update(id, requestState)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}