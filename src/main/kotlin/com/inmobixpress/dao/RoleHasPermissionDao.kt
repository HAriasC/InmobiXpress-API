package com.inmobixpress.dao

import com.inmobixpress.dao.PermissionDaoImpl.toPermission
import com.inmobixpress.dao.RoleDaoImpl.toRole
import com.inmobixpress.dao.SpaceDaoImpl.toSpace
import com.inmobixpress.database.query
import com.inmobixpress.model.Permissions
import com.inmobixpress.model.RoleHasPermission
import com.inmobixpress.model.Roles
import com.inmobixpress.model.RolesHasPermission
import com.inmobixpress.model.Spaces
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface RoleHasPermissionDao {
    suspend fun getAll(): List<RoleHasPermission>
    suspend fun findById(roleId: Int, spaceId: Int, permissionId: Int): RoleHasPermission?
    suspend fun insert(roleHasPermission: RoleHasPermission): Boolean
    suspend fun update(
        roleId: Int,
        spaceId: Int,
        permissionId: Int,
        roleHasPermission: RoleHasPermission
    ): Boolean

    suspend fun delete(roleId: Int, spaceId: Int, permissionId: Int): Boolean
}

object RoleHasPermissionDaoImpl : RoleHasPermissionDao {
    override suspend fun getAll(): List<RoleHasPermission> = query {
        RolesHasPermission.selectAll().map { it.toRoleHasPermission() }
    }

    override suspend fun findById(
        roleId: Int,
        spaceId: Int,
        permissionId: Int
    ): RoleHasPermission? = query {
        RolesHasPermission.selectAll().where {
            RolesHasPermission.role eq roleId and (
                    RolesHasPermission.space eq spaceId and (
                            RolesHasPermission.permission eq permissionId))
        }.map { it.toRoleHasPermission() }.singleOrNull()
    }

    override suspend fun insert(roleHasPermission: RoleHasPermission): Boolean = query {
        RolesHasPermission.selectAll().where {
            Roles.id eq roleHasPermission.role.id
        }.map { it.toRole() }.singleOrNull() ?: return@query false
        Spaces.selectAll().where {
            Spaces.id eq roleHasPermission.space.id
        }.map { it.toSpace() }.singleOrNull() ?: return@query false
        Permissions.selectAll().where {
            Permissions.id eq roleHasPermission.permission.id
        }.map { it.toPermission() }.singleOrNull() ?: return@query false
        RolesHasPermission.insert {
            it[role] = roleHasPermission.role.id
            it[space] = roleHasPermission.space.id
            it[permission] = roleHasPermission.permission.id
        }
        return@query true
    }

    override suspend fun update(
        roleId: Int,
        spaceId: Int,
        permissionId: Int,
        roleHasPermission: RoleHasPermission
    ): Boolean = query {
        RolesHasPermission.selectAll().where {
            Roles.id eq roleHasPermission.role.id
        }.map { it.toRole() }.singleOrNull() ?: return@query false
        Spaces.selectAll().where {
            Spaces.id eq roleHasPermission.space.id
        }.map { it.toSpace() }.singleOrNull() ?: return@query false
        Permissions.selectAll().where {
            Permissions.id eq roleHasPermission.permission.id
        }.map { it.toPermission() }.singleOrNull() ?: return@query false
        RolesHasPermission.update({
            RolesHasPermission.role eq roleId and (
                    RolesHasPermission.space eq spaceId and (
                            RolesHasPermission.permission eq permissionId))
        }) {
            it[role] = roleHasPermission.role.id
            it[space] = roleHasPermission.space.id
            it[permission] = roleHasPermission.permission.id
        } == 1
    }

    override suspend fun delete(roleId: Int, spaceId: Int, permissionId: Int): Boolean = query {
        RolesHasPermission.deleteWhere {
            role eq roleId and (space eq spaceId and (permission eq permissionId))
        } > 0
    }

    fun ResultRow.toRoleHasPermission() = RoleHasPermission(
        role = findRole(this[RolesHasPermission.role].value),
        space = findSpace(this[RolesHasPermission.space].value),
        permission = findPermission(this[RolesHasPermission.permission].value)
    )

    private fun findRole(id: Int) = Roles.selectAll().where {
        Roles.id eq id
    }.map { it.toRole() }.single()

    private fun findSpace(id: Int) = Spaces.selectAll().where {
        Spaces.id eq id
    }.map { it.toSpace() }.single()

    private fun findPermission(id: Int) = Permissions.selectAll().where {
        Permissions.id eq id
    }.map { it.toPermission() }.single()
}