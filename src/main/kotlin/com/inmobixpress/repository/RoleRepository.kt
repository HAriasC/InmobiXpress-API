package com.inmobixpress.repository

import com.inmobixpress.dao.RoleDao
import com.inmobixpress.dao.RoleDaoImpl
import com.inmobixpress.model.Role

class RoleRepository(
    private val dao: RoleDao = RoleDaoImpl
) {
    suspend fun getAll(): List<Role> = dao.getAll()
    suspend fun findById(id: Int): Role? = dao.findById(id)
    suspend fun insert(role: Role) = dao.insert(role)
    suspend fun update(id: Int, role: Role) = dao.update(id, role)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}