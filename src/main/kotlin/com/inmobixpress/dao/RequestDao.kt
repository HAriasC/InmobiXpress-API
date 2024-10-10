package com.inmobixpress.dao

import com.inmobixpress.dao.RequestStateDaoImpl.toRequestState
import com.inmobixpress.dao.RequestTypeDaoImpl.toRequestType
import com.inmobixpress.dao.UserDaoImpl.toUser
import com.inmobixpress.database.query
import com.inmobixpress.model.Request
import com.inmobixpress.model.RequestStates
import com.inmobixpress.model.RequestTypes
import com.inmobixpress.model.Requests
import com.inmobixpress.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface RequestDao {
    suspend fun getAll(): List<Request>
    suspend fun findById(id: Int): Request?
    suspend fun insert(request: Request): Boolean
    suspend fun update(id: Int, request: Request): Boolean
    suspend fun delete(id: Int): Boolean
}

object RequestDaoImpl : RequestDao {
    override suspend fun getAll(): List<Request> = query {
        Requests.selectAll().map { it.toRequest() }
    }

    override suspend fun findById(id: Int): Request? = query {
        Requests.selectAll().where {
            Requests.id eq id
        }.map { it.toRequest() }.singleOrNull()
    }

    override suspend fun insert(request: Request): Boolean = query {
        val requestTypeId = RequestTypes.selectAll().where {
            RequestTypes.id eq request.requestType.id
        }.map { it.toRequestType() }.singleOrNull() ?: return@query false
        val requestStateId = RequestStates.selectAll().where {
            RequestStates.id eq request.requestState.id
        }.map { it.toRequestState() }.singleOrNull() ?: return@query false
        val userId = Users.selectAll().where {
            Users.id eq request.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Requests.insert {
            it[date] = request.date
            it[message] = request.message
            it[requestType] = requestTypeId.id
            it[requestState] = requestStateId.id
            it[user] = userId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, request: Request): Boolean = query {
        val requestTypeId = RequestTypes.selectAll().where {
            RequestTypes.id eq request.requestType.id
        }.map { it.toRequestType() }.singleOrNull() ?: return@query false
        val requestStateId = RequestStates.selectAll().where {
            RequestStates.id eq request.requestState.id
        }.map { it.toRequestState() }.singleOrNull() ?: return@query false
        val userId = Users.selectAll().where {
            Users.id eq request.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Requests.update({ Requests.id eq id }) {
            it[date] = request.date
            it[message] = request.message
            it[requestType] = requestTypeId.id
            it[requestState] = requestStateId.id
            it[user] = userId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Requests.deleteWhere {
            Requests.id eq id
        } > 0
    }

    fun ResultRow.toRequest() = Request(
        id = this[Requests.id].value,
        date = this[Requests.date],
        message = this[Requests.message],
        requestType = findRequestType(id = this[Requests.requestType].value),
        requestState = findRequestState(id = this[Requests.requestState].value),
        user = findUser(id = this[Requests.user].value)
    )

    private fun findRequestType(id: Int) = RequestTypes.selectAll().where {
        RequestTypes.id eq id
    }.map { it.toRequestType() }.single()

    private fun findRequestState(id: Int) = RequestStates.selectAll().where {
        RequestStates.id eq id
    }.map { it.toRequestState() }.single()

    private fun findUser(id: Int) = Users.selectAll().where {
        Users.id eq id
    }.map { it.toUser() }.single()
}