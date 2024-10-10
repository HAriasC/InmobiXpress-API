package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.RequestType
import com.inmobixpress.model.RequestTypes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface RequestTypeDao {
    suspend fun getAll(): List<RequestType>
    suspend fun findById(id: Int): RequestType?
    suspend fun insert(requestType: RequestType)
    suspend fun update(id: Int, requestType: RequestType): Boolean
    suspend fun delete(id: Int): Boolean
}

object RequestTypeDaoImpl : RequestTypeDao {
    override suspend fun getAll(): List<RequestType> = query {
        RequestTypes.selectAll().map { it.toRequestType() }
    }

    override suspend fun findById(id: Int): RequestType? = query {
        RequestTypes.selectAll().where {
            RequestTypes.id eq id
        }.map { it.toRequestType() }.singleOrNull()
    }

    override suspend fun insert(requestType: RequestType): Unit = query {
        RequestTypes.insert {
            it[name] = requestType.name
        }
    }

    override suspend fun update(id: Int, requestType: RequestType): Boolean = query {
        RequestTypes.update({ RequestTypes.id eq id }) {
            it[name] = requestType.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        RequestTypes.deleteWhere {
            RequestTypes.id eq id
        } > 0
    }

    fun ResultRow.toRequestType() = RequestType(
        id = this[RequestTypes.id].value,
        name = this[RequestTypes.name]
    )
}