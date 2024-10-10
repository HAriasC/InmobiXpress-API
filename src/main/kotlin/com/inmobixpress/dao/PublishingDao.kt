package com.inmobixpress.dao

import com.inmobixpress.dao.PropertyDaoImpl.toProperty
import com.inmobixpress.database.query
import com.inmobixpress.model.Properties
import com.inmobixpress.model.Publishing
import com.inmobixpress.model.Publishings
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface PublishingDao {
    suspend fun getAll(): List<Publishing>
    suspend fun findById(id: Int): Publishing?
    suspend fun insert(publishing: Publishing): Boolean
    suspend fun update(id: Int, publishing: Publishing): Boolean
    suspend fun delete(id: Int): Boolean
}

object PublishingDaoImpl : PublishingDao {
    override suspend fun getAll(): List<Publishing> = query {
        Publishings.selectAll().map { it.toPublishing() }
    }

    override suspend fun findById(id: Int): Publishing? = query {
        Publishings.selectAll().where {
            Publishings.id eq id
        }.map { it.toPublishing() }.singleOrNull()
    }

    override suspend fun insert(publishing: Publishing): Boolean = query {
        val propertyId = Properties.selectAll().where {
            Properties.id eq publishing.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        Publishings.insert {
            it[numberView] = publishing.numberView
            it[property] = propertyId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, publishing: Publishing): Boolean = query {
        val propertyId = Properties.selectAll().where {
            Properties.id eq publishing.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        Publishings.update({ Publishings.id eq id }) {
            it[numberView] = publishing.numberView
            it[property] = propertyId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Publishings.deleteWhere {
            Publishings.id eq id
        } > 0
    }

    fun ResultRow.toPublishing() = Publishing(
        id = this[Publishings.id].value,
        numberView = this[Publishings.numberView],
        property = findProperty(id = this[Publishings.property].value)
    )

    private fun findProperty(id: Int) = Properties.selectAll().where {
        Properties.id eq id
    }.map { it.toProperty() }.single()
}