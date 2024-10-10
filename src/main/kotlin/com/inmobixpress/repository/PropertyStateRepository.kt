package com.inmobixpress.repository

import com.inmobixpress.dao.PropertyStateDao
import com.inmobixpress.dao.PropertyStateDaoImpl
import com.inmobixpress.model.PropertyState

class PropertyStateRepository(
    private val dao: PropertyStateDao = PropertyStateDaoImpl
) {
    suspend fun getAll(): List<PropertyState> = dao.getAll()
    suspend fun findById(id: Int): PropertyState? = dao.findById(id)
    suspend fun insert(propertyState: PropertyState) = dao.insert(propertyState)
    suspend fun update(id: Int, propertyState: PropertyState) = dao.update(id, propertyState)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}