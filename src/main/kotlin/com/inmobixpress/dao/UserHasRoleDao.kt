package com.inmobixpress.dao

import com.inmobixpress.dao.RoleDaoImpl.toRole
import com.inmobixpress.dao.UserDaoImpl.toUser
import com.inmobixpress.database.query
import com.inmobixpress.model.Roles
import com.inmobixpress.model.UserHasRole
import com.inmobixpress.model.Users
import com.inmobixpress.model.UsersHasRole
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface UserHasRoleDao {
    suspend fun getAll(): List<UserHasRole>
    suspend fun findById(userId: Int, roleId: Int): UserHasRole?
    suspend fun insert(userHasRole: UserHasRole): Boolean
    suspend fun update(
        userId: Int,
        roleId: Int,
        userHasRole: UserHasRole
    ): Boolean

    suspend fun delete(userId: Int, roleId: Int): Boolean
}

object UserHasRoleDaoImpl : UserHasRoleDao {
    override suspend fun getAll(): List<UserHasRole> = query {
        UsersHasRole.selectAll().map { it.toUserHasRole() }
    }

    override suspend fun findById(userId: Int, roleId: Int): UserHasRole? = query {
        UsersHasRole.selectAll().where {
            UsersHasRole.user eq userId and (
                    UsersHasRole.role eq roleId)
        }.map { it.toUserHasRole() }.singleOrNull()
    }

    override suspend fun insert(userHasRole: UserHasRole): Boolean = query {
        Users.selectAll().where {
            Users.id eq userHasRole.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Roles.selectAll().where {
            Roles.id eq userHasRole.role.id
        }.map { it.toRole() }.singleOrNull() ?: return@query false
        UsersHasRole.insert {
            it[user] = userHasRole.user.id
            it[role] = userHasRole.role.id
        }
        return@query true
    }

    override suspend fun update(
        userId: Int,
        roleId: Int,
        userHasRole: UserHasRole
    ): Boolean = query {
        Users.selectAll().where {
            Users.id eq userHasRole.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Roles.selectAll().where {
            Roles.id eq userHasRole.role.id
        }.map { it.toRole() }.singleOrNull() ?: return@query false
        UsersHasRole.update({
            UsersHasRole.user eq userId and (
                    UsersHasRole.role eq roleId)
        }) {
            it[user] = userHasRole.user.id
            it[role] = userHasRole.role.id
        } == 1
    }

    override suspend fun delete(userId: Int, roleId: Int): Boolean = query {
        UsersHasRole.deleteWhere {
            user eq userId and (role eq roleId)
        } > 0
    }

    fun ResultRow.toUserHasRole() = UserHasRole(
        user = findUser(this[UsersHasRole.user].value),
        role = findRole(this[UsersHasRole.role].value)
    )

    private fun findUser(id: Int) = Users.selectAll().where {
        Users.id eq id
    }.map { it.toUser() }.single()

    private fun findRole(id: Int) = Roles.selectAll().where {
        Roles.id eq id
    }.map { it.toRole() }.single()
}