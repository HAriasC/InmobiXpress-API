package com.inmobixpress.repository

import com.inmobixpress.dao.PermissionDao
import com.inmobixpress.dao.PermissionDaoImpl
import com.inmobixpress.model.Permission

class PermissionRepository(
    private val dao: PermissionDao = PermissionDaoImpl
) {
    suspend fun getAll(): List<Permission> = dao.getAll()
    suspend fun findById(id: Int): Permission? = dao.findById(id)
    suspend fun insert(permission: Permission) = dao.insert(permission)
    suspend fun update(id: Int, permission: Permission) = dao.update(id, permission)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}