package com.inmobixpress.repository

import com.inmobixpress.dao.DepartmentDao
import com.inmobixpress.dao.DepartmentDaoImpl
import com.inmobixpress.model.Department

class DepartmentRepository(
    private val dao: DepartmentDao = DepartmentDaoImpl
) {
    suspend fun getAll(): List<Department> = dao.getAll()
    suspend fun findById(id: Int): Department? = dao.findById(id)
    suspend fun insert(department: Department) = dao.insert(department)
    suspend fun update(id: Int, department: Department) = dao.update(id, department)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}