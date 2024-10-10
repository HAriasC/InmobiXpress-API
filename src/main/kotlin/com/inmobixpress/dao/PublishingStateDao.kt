package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.PublishingState
import com.inmobixpress.model.PublishingStates
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface PublishingStateDao {
    suspend fun getAll(): List<PublishingState>
    suspend fun findById(id: Int): PublishingState?
    suspend fun insert(publishingState: PublishingState)
    suspend fun update(id: Int, publishingState: PublishingState): Boolean
    suspend fun delete(id: Int): Boolean
}

object PublishingStateDaoImpl : PublishingStateDao {
    override suspend fun getAll(): List<PublishingState> = query {
        PublishingStates.selectAll().map { it.toPublishingState() }
    }

    override suspend fun findById(id: Int): PublishingState? = query {
        PublishingStates.selectAll().where {
            PublishingStates.id eq id
        }.map { it.toPublishingState() }.singleOrNull()
    }

    override suspend fun insert(publishingState: PublishingState): Unit = query {
        PublishingStates.insert {
            it[name] = publishingState.name
        }
    }

    override suspend fun update(id: Int, publishingState: PublishingState): Boolean = query {
        PublishingStates.update({ PublishingStates.id eq id }) {
            it[name] = publishingState.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        PublishingStates.deleteWhere {
            PublishingStates.id eq id
        } > 0
    }

    fun ResultRow.toPublishingState() = PublishingState(
        id = this[PublishingStates.id].value,
        name = this[PublishingStates.name]
    )
}