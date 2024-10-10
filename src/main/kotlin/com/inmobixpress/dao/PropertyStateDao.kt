package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.PropertyState
import com.inmobixpress.model.PropertyStates
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface PropertyStateDao {
    suspend fun getAll(): List<PropertyState>
    suspend fun findById(id: Int): PropertyState?
    suspend fun insert(propertyState: PropertyState)
    suspend fun update(id: Int, propertyState: PropertyState): Boolean
    suspend fun delete(id: Int): Boolean
}

object PropertyStateDaoImpl : PropertyStateDao {
    override suspend fun getAll(): List<PropertyState> = query {
        PropertyStates.selectAll().map { it.toPropertyState() }
    }

    override suspend fun findById(id: Int): PropertyState? = query {
        PropertyStates.selectAll().where {
            PropertyStates.id eq id
        }.map { it.toPropertyState() }.singleOrNull()
    }

    override suspend fun insert(propertyState: PropertyState): Unit = query {
        PropertyStates.insert {
            it[name] = propertyState.name
        }
    }

    override suspend fun update(id: Int, propertyState: PropertyState): Boolean = query {
        PropertyStates.update({ PropertyStates.id eq id }) {
            it[name] = propertyState.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        PropertyStates.deleteWhere {
            PropertyStates.id eq id
        } > 0
    }

    fun ResultRow.toPropertyState() = PropertyState(
        id = this[PropertyStates.id].value,
        name = this[PropertyStates.name]
    )
}