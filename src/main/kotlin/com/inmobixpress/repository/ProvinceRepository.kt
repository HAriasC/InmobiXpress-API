package com.inmobixpress.repository

import com.inmobixpress.dao.ProvinceDao
import com.inmobixpress.dao.ProvinceDaoImpl
import com.inmobixpress.model.Province

class ProvinceRepository(
    private val dao: ProvinceDao = ProvinceDaoImpl
) {
    suspend fun getAll(): List<Province> = dao.getAll()
    suspend fun findById(id: Int): Province? = dao.findById(id)
    suspend fun insert(province: Province) = dao.insert(province)
    suspend fun update(id: Int, province: Province) = dao.update(id, province)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}