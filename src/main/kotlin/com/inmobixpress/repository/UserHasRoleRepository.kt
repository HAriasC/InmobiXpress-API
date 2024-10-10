package com.inmobixpress.repository

import com.inmobixpress.dao.UserHasRoleDao
import com.inmobixpress.dao.UserHasRoleDaoImpl
import com.inmobixpress.model.UserHasRole

class UserHasRoleRepository(
    private val dao: UserHasRoleDao = UserHasRoleDaoImpl
) {
    suspend fun getAll(): List<UserHasRole> = dao.getAll()
    suspend fun findById(userId: Int, roleId: Int): UserHasRole? =
        dao.findById(userId, roleId)

    suspend fun insert(userHasRole: UserHasRole) = dao.insert(userHasRole)
    suspend fun update(userId: Int, roleId: Int, userHasRole: UserHasRole) =
        dao.update(userId, roleId, userHasRole)

    suspend fun delete(userId: Int, roleId: Int): Boolean =
        dao.delete(userId, roleId)
}