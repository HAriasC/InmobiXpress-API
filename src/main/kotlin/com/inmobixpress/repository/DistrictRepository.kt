package com.inmobixpress.repository

import com.inmobixpress.dao.DistricitDaoImpl
import com.inmobixpress.dao.DistrictDao
import com.inmobixpress.model.District

class DistrictRepository(
    private val dao: DistrictDao = DistricitDaoImpl
) {
    suspend fun getAll(): List<District> = dao.getAll()
    suspend fun findById(id: Int): District? = dao.findById(id)
    suspend fun insert(district: District) = dao.insert(district)
    suspend fun update(id: Int, district: District) = dao.update(id, district)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}