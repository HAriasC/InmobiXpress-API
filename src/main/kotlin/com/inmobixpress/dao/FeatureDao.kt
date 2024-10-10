package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.Feature
import com.inmobixpress.model.Features
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface FeatureDao {
    suspend fun getAll(): List<Feature>
    suspend fun findById(id: Int): Feature?
    suspend fun insert(feature: Feature)
    suspend fun update(id: Int, feature: Feature): Boolean
    suspend fun delete(id: Int): Boolean
}

object FeatureDaoImpl : FeatureDao {
    override suspend fun getAll(): List<Feature> = query {
        Features.selectAll().map { it.toFeature() }
    }

    override suspend fun findById(id: Int): Feature? = query {
        Features.selectAll().where {
            Features.id eq id
        }.map { it.toFeature() }.singleOrNull()
    }

    override suspend fun insert(feature: Feature): Unit = query {
        Features.insert {
            it[name] = feature.name
            it[priority] = feature.priority
        }
    }

    override suspend fun update(id: Int, feature: Feature): Boolean = query {
        Features.update({ Features.id eq id }) {
            it[name] = feature.name
            it[priority] = feature.priority
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Features.deleteWhere {
            Features.id eq id
        } > 0
    }

    fun ResultRow.toFeature() = Feature(
        id = this[Features.id].value,
        name = this[Features.name],
        priority = this[Features.priority]
    )
}