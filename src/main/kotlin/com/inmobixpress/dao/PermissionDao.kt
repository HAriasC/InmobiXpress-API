package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.Permission
import com.inmobixpress.model.Permissions
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface PermissionDao {
    suspend fun getAll(): List<Permission>
    suspend fun findById(id: Int): Permission?
    suspend fun insert(permission: Permission)
    suspend fun update(id: Int, permission: Permission): Boolean
    suspend fun delete(id: Int): Boolean
}

object PermissionDaoImpl : PermissionDao {
    override suspend fun getAll(): List<Permission> = query {
        Permissions.selectAll().map { it.toPermission() }
    }

    override suspend fun findById(id: Int): Permission? = query {
        Permissions.selectAll().where {
            Permissions.id eq id
        }.map { it.toPermission() }.singleOrNull()
    }

    override suspend fun insert(permission: Permission): Unit = query {
        Permissions.insert {
            it[name] = permission.name
        }
    }

    override suspend fun update(id: Int, permission: Permission): Boolean = query {
        Permissions.update({ Permissions.id eq id }) {
            it[name] = permission.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Permissions.deleteWhere {
            Permissions.id eq id
        } > 0
    }

    fun ResultRow.toPermission() = Permission(
        id = this[Permissions.id].value,
        name = this[Permissions.name]
    )
}