package com.inmobixpress.repository

import com.inmobixpress.dao.PropertyDao
import com.inmobixpress.dao.PropertyDaoImpl
import com.inmobixpress.model.Property

class PropertyRepository(
    private val dao: PropertyDao = PropertyDaoImpl
) {
    suspend fun getAll(): List<Property> = dao.getAll()
    suspend fun findById(id: Int): Property? = dao.findById(id)
    suspend fun insert(property: Property) = dao.insert(property)
    suspend fun update(id: Int, property: Property) = dao.update(id, property)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}