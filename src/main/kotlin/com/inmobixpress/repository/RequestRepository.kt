package com.inmobixpress.repository

import com.inmobixpress.dao.RequestDao
import com.inmobixpress.dao.RequestDaoImpl
import com.inmobixpress.model.Request

class RequestRepository(
    private val dao: RequestDao = RequestDaoImpl
) {
    suspend fun getAll(): List<Request> = dao.getAll()
    suspend fun findById(id: Int): Request? = dao.findById(id)
    suspend fun insert(request: Request) = dao.insert(request)
    suspend fun update(id: Int, request: Request) = dao.update(id, request)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}