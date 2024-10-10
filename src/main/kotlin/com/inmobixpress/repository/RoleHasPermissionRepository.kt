package com.inmobixpress.repository

import com.inmobixpress.dao.RoleHasPermissionDao
import com.inmobixpress.dao.RoleHasPermissionDaoImpl
import com.inmobixpress.model.RoleHasPermission

class RoleHasPermissionRepository(
    private val dao: RoleHasPermissionDao = RoleHasPermissionDaoImpl
) {
    suspend fun getAll(): List<RoleHasPermission> = dao.getAll()
    suspend fun findById(roleId: Int, spaceId: Int, permissionId: Int): RoleHasPermission? =
        dao.findById(roleId, spaceId, permissionId)

    suspend fun insert(roleHasPermission: RoleHasPermission) = dao.insert(roleHasPermission)
    suspend fun update(
        roleId: Int,
        spaceId: Int,
        permissionId: Int,
        roleHasPermission: RoleHasPermission
    ) =
        dao.update(roleId, spaceId, permissionId, roleHasPermission)

    suspend fun delete(roleId: Int, spaceId: Int, permissionId: Int): Boolean =
        dao.delete(roleId, spaceId, permissionId)
}