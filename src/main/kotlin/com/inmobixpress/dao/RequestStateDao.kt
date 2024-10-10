package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.RequestState
import com.inmobixpress.model.RequestStates
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface RequestStateDao {
    suspend fun getAll(): List<RequestState>
    suspend fun findById(id: Int): RequestState?
    suspend fun insert(requestState: RequestState)
    suspend fun update(id: Int, requestState: RequestState): Boolean
    suspend fun delete(id: Int): Boolean
}

object RequestStateDaoImpl : RequestStateDao {
    override suspend fun getAll(): List<RequestState> = query {
        RequestStates.selectAll().map { it.toRequestState() }
    }

    override suspend fun findById(id: Int): RequestState? = query {
        RequestStates.selectAll().where {
            RequestStates.id eq id
        }.map { it.toRequestState() }.singleOrNull()
    }

    override suspend fun insert(requestState: RequestState): Unit = query {
        RequestStates.insert {
            it[name] = requestState.name
        }
    }

    override suspend fun update(id: Int, requestState: RequestState): Boolean = query {
        RequestStates.update({ RequestStates.id eq id }) {
            it[name] = requestState.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        RequestStates.deleteWhere {
            RequestStates.id eq id
        } > 0
    }

    fun ResultRow.toRequestState() = RequestState(
        id = this[RequestStates.id].value,
        name = this[RequestStates.name]
    )
}