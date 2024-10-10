package com.inmobixpress.dao

import com.inmobixpress.dao.UserDaoImpl.toUser
import com.inmobixpress.database.query
import com.inmobixpress.model.Device
import com.inmobixpress.model.Devices
import com.inmobixpress.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface DeviceDao {
    suspend fun getAll(): List<Device>
    suspend fun findById(id: Int): Device?
    suspend fun insert(device: Device): Boolean
    suspend fun update(id: Int, device: Device): Boolean
    suspend fun delete(id: Int): Boolean
}

object DeviceDaoImpl : DeviceDao {
    override suspend fun getAll(): List<Device> = query {
        Devices.selectAll().map { it.toDevice() }
    }

    override suspend fun findById(id: Int): Device? = query {
        Devices.selectAll().where {
            Devices.id eq id
        }.map { it.toDevice() }.singleOrNull()
    }

    override suspend fun insert(device: Device): Boolean = query {
        val userId = Users.selectAll().where {
            Users.id eq device.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Devices.insert {
            it[phone] = device.phone
            it[token] = device.token
            it[user] = userId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, device: Device): Boolean = query {
        val userId = Users.selectAll().where {
            Users.id eq device.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Devices.update({ Devices.id eq id }) {
            it[phone] = device.phone
            it[token] = device.token
            it[user] = userId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Devices.deleteWhere {
            Devices.id eq id
        } > 0
    }

    fun ResultRow.toDevice() = Device(
        id = this[Devices.id].value,
        phone = this[Devices.phone],
        token = this[Devices.token],
        user = findUser(id = this[Devices.user].value)
    )

    private fun findUser(id: Int) = Users.selectAll().where {
        Users.id eq id
    }.map { it.toUser() }.single()
}