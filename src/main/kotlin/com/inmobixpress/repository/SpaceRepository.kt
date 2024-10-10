package com.inmobixpress.repository

import com.inmobixpress.dao.SpaceDao
import com.inmobixpress.dao.SpaceDaoImpl
import com.inmobixpress.model.Space

class SpaceRepository(
    private val dao: SpaceDao = SpaceDaoImpl
) {
    suspend fun getAll(): List<Space> = dao.getAll()
    suspend fun findById(id: Int): Space? = dao.findById(id)
    suspend fun insert(space: Space) = dao.insert(space)
    suspend fun update(id: Int, space: Space) = dao.update(id, space)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}