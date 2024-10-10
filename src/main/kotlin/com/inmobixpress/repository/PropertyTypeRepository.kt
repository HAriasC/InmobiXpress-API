package com.inmobixpress.repository

import com.inmobixpress.dao.PropertyTypeDao
import com.inmobixpress.dao.PropertyTypeDaoImpl
import com.inmobixpress.model.PropertyType

class PropertyTypeRepository(
    private val dao: PropertyTypeDao = PropertyTypeDaoImpl
) {
    suspend fun getAll(): List<PropertyType> = dao.getAll()
    suspend fun findById(id: Int): PropertyType? = dao.findById(id)
    suspend fun insert(propertyType: PropertyType) = dao.insert(propertyType)
    suspend fun update(id: Int, propertyType: PropertyType) = dao.update(id, propertyType)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}