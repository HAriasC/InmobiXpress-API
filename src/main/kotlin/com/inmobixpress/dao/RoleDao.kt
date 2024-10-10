package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.Role
import com.inmobixpress.model.Roles
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface RoleDao {
    suspend fun getAll(): List<Role>
    suspend fun findById(id: Int): Role?
    suspend fun insert(role: Role)
    suspend fun update(id: Int, role: Role): Boolean
    suspend fun delete(id: Int): Boolean
}

object RoleDaoImpl : RoleDao {
    override suspend fun getAll(): List<Role> = query {
        Roles.selectAll().map { it.toRole() }
    }

    override suspend fun findById(id: Int): Role? = query {
        Roles.selectAll().where {
            Roles.id eq id
        }.map { it.toRole() }.singleOrNull()
    }

    override suspend fun insert(role: Role): Unit = query {
        Roles.insert {
            it[name] = role.name
        }
    }

    override suspend fun update(id: Int, role: Role): Boolean = query {
        Roles.update({ Roles.id eq id }) {
            it[name] = role.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Roles.deleteWhere {
            Roles.id eq id
        } > 0
    }

    fun ResultRow.toRole() = Role(
        id = this[Roles.id].value,
        name = this[Roles.name]
    )
}