package com.inmobixpress.dao

import com.inmobixpress.dao.PublishingDaoImpl.toPublishing
import com.inmobixpress.dao.RequestDaoImpl.toRequest
import com.inmobixpress.database.query
import com.inmobixpress.model.Publishings
import com.inmobixpress.model.RequestHasPublishing
import com.inmobixpress.model.Requests
import com.inmobixpress.model.RequestsHasPublishing
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface RequestHasPublishingDao {
    suspend fun getAll(): List<RequestHasPublishing>
    suspend fun findById(requestId: Int, publishingId: Int): RequestHasPublishing?
    suspend fun insert(requestHasPublishing: RequestHasPublishing): Boolean
    suspend fun update(
        requestId: Int,
        publishingId: Int,
        requestHasPublishing: RequestHasPublishing
    ): Boolean

    suspend fun delete(requestId: Int, publishingId: Int): Boolean
}

object RequestHasPublishingDaoImpl : RequestHasPublishingDao {
    override suspend fun getAll(): List<RequestHasPublishing> = query {
        RequestsHasPublishing.selectAll().map { it.toRequestHasPublishing() }
    }

    override suspend fun findById(
        requestId: Int,
        publishingId: Int
    ): RequestHasPublishing? = query {
        RequestsHasPublishing.selectAll().where {
            RequestsHasPublishing.request eq requestId and (
                    RequestsHasPublishing.publishing eq publishingId)
        }.map { it.toRequestHasPublishing() }.singleOrNull()
    }

    override suspend fun insert(requestHasPublishing: RequestHasPublishing): Boolean = query {
        Requests.selectAll().where {
            Requests.id eq requestHasPublishing.request.id
        }.map { it.toRequest() }.singleOrNull() ?: return@query false
        Publishings.selectAll().where {
            Publishings.id eq requestHasPublishing.publishing.id
        }.map { it.toPublishing() }.singleOrNull() ?: return@query false
        RequestsHasPublishing.insert {
            it[request] = requestHasPublishing.request.id
            it[publishing] = requestHasPublishing.publishing.id
            it[createDate] = requestHasPublishing.createDate
        }
        return@query true
    }

    override suspend fun update(
        requestId: Int,
        publishingId: Int,
        requestHasPublishing: RequestHasPublishing
    ): Boolean = query {
        Requests.selectAll().where {
            Requests.id eq requestHasPublishing.request.id
        }.map { it.toRequest() }.singleOrNull() ?: return@query false
        Publishings.selectAll().where {
            Publishings.id eq requestHasPublishing.publishing.id
        }.map { it.toPublishing() }.singleOrNull() ?: return@query false
        RequestsHasPublishing.update({
            RequestsHasPublishing.request eq requestId and (
                    RequestsHasPublishing.publishing eq publishingId)
        }) {
            it[request] = requestHasPublishing.request.id
            it[publishing] = requestHasPublishing.publishing.id
            it[createDate] = requestHasPublishing.createDate
        } == 1
    }

    override suspend fun delete(requestId: Int, publishingId: Int): Boolean = query {
        RequestsHasPublishing.deleteWhere {
            request eq requestId and (publishing eq publishingId)
        } > 0
    }

    fun ResultRow.toRequestHasPublishing() = RequestHasPublishing(
        request = findRequest(this[RequestsHasPublishing.request].value),
        publishing = findPublishing(this[RequestsHasPublishing.publishing].value),
        createDate = this[RequestsHasPublishing.createDate]
    )

    private fun findRequest(id: Int) = Requests.selectAll().where {
        Requests.id eq id
    }.map { it.toRequest() }.single()

    private fun findPublishing(id: Int) = Publishings.selectAll().where {
        Publishings.id eq id
    }.map { it.toPublishing() }.single()
}