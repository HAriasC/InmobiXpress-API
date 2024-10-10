package com.inmobixpress.dao

import com.inmobixpress.dao.PublishingDaoImpl.toPublishing
import com.inmobixpress.dao.PublishingStateDaoImpl.toPublishingState
import com.inmobixpress.database.query
import com.inmobixpress.model.Historical
import com.inmobixpress.model.Historicals
import com.inmobixpress.model.PublishingStates
import com.inmobixpress.model.Publishings
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface HistoricalDao {
    suspend fun getAll(): List<Historical>
    suspend fun findById(publishingStateId: Int, publishingId: Int): Historical?
    suspend fun insert(historical: Historical): Boolean
    suspend fun update(
        publishingStateId: Int,
        publishingId: Int,
        historical: Historical
    ): Boolean

    suspend fun delete(publishingStateId: Int, publishingId: Int): Boolean
}

object HistoricalDaoImpl : HistoricalDao {
    override suspend fun getAll(): List<Historical> = query {
        Historicals.selectAll().map { it.toHistorical() }
    }

    override suspend fun findById(publishingStateId: Int, publishingId: Int): Historical? = query {
        Historicals.selectAll().where {
            Historicals.publishingState eq publishingStateId and (
                    Historicals.publishing eq publishingId)
        }.map { it.toHistorical() }.singleOrNull()
    }

    override suspend fun insert(historical: Historical): Boolean = query {
        PublishingStates.selectAll().where {
            PublishingStates.id eq historical.publishingState.id
        }.map { it.toPublishingState() }.singleOrNull() ?: return@query false
        Publishings.selectAll().where {
            Publishings.id eq historical.publishing.id
        }.map { it.toPublishing() }.singleOrNull() ?: return@query false
        Historicals.insert {
            it[publishingState] = historical.publishingState.id
            it[publishing] = historical.publishing.id
            it[date] = historical.date
        }
        return@query true
    }

    override suspend fun update(
        publishingStateId: Int,
        publishingId: Int,
        historical: Historical
    ): Boolean = query {
        PublishingStates.selectAll().where {
            PublishingStates.id eq historical.publishingState.id
        }.map { it.toPublishingState() }.singleOrNull() ?: return@query false
        Publishings.selectAll().where {
            Publishings.id eq historical.publishing.id
        }.map { it.toPublishing() }.singleOrNull() ?: return@query false
        Historicals.update({
            Historicals.publishingState eq publishingStateId and (
                    Historicals.publishing eq publishingId)
        }) {
            it[publishingState] = historical.publishingState.id
            it[publishing] = historical.publishing.id
            it[date] = historical.date
        } == 1
    }

    override suspend fun delete(publishingStateId: Int, publishingId: Int): Boolean = query {
        Historicals.deleteWhere {
            publishingState eq publishingStateId and (publishing eq publishingId)
        } > 0
    }

    fun ResultRow.toHistorical() = Historical(
        publishingState = findPublishingState(this[Historicals.publishingState].value),
        publishing = findPublishing(this[Historicals.publishing].value),
        date = this[Historicals.date]
    )

    private fun findPublishingState(id: Int) = PublishingStates.selectAll().where {
        PublishingStates.id eq id
    }.map { it.toPublishingState() }.single()

    private fun findPublishing(id: Int) = Publishings.selectAll().where {
        Publishings.id eq id
    }.map { it.toPublishing() }.single()
}