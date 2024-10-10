package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.PropertyType
import com.inmobixpress.model.PropertyTypes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface PropertyTypeDao {
    suspend fun getAll(): List<PropertyType>
    suspend fun findById(id: Int): PropertyType?
    suspend fun insert(propertyType: PropertyType)
    suspend fun update(id: Int, propertyType: PropertyType): Boolean
    suspend fun delete(id: Int): Boolean
}

object PropertyTypeDaoImpl : PropertyTypeDao {
    override suspend fun getAll(): List<PropertyType> = query {
        PropertyTypes.selectAll().map { it.toPropertyType() }
    }

    override suspend fun findById(id: Int): PropertyType? = query {
        PropertyTypes.selectAll().where {
            PropertyTypes.id eq id
        }.map { it.toPropertyType() }.singleOrNull()
    }

    override suspend fun insert(propertyType: PropertyType): Unit = query {
        PropertyTypes.insert {
            it[name] = propertyType.name
        }
    }

    override suspend fun update(id: Int, propertyType: PropertyType): Boolean = query {
        PropertyTypes.update({ PropertyTypes.id eq id }) {
            it[name] = propertyType.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        PropertyTypes.deleteWhere {
            PropertyTypes.id eq id
        } > 0
    }

    fun ResultRow.toPropertyType() = PropertyType(
        id = this[PropertyTypes.id].value,
        name = this[PropertyTypes.name]
    )
}