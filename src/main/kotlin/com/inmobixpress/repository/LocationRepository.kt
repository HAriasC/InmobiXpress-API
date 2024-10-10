package com.inmobixpress.repository

import com.inmobixpress.dao.LocationDao
import com.inmobixpress.dao.LocationDaoImpl
import com.inmobixpress.model.Location

class LocationRepository(
    private val dao: LocationDao = LocationDaoImpl
) {
    suspend fun getAll(): List<Location> = dao.getAll()
    suspend fun findById(id: Int): Location? = dao.findById(id)
    suspend fun insert(location: Location) = dao.insert(location)
    suspend fun update(id: Int, location: Location) = dao.update(id, location)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}