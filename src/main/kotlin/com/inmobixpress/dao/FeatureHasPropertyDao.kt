package com.inmobixpress.dao

import com.inmobixpress.dao.FeatureDaoImpl.toFeature
import com.inmobixpress.dao.PropertyDaoImpl.toProperty
import com.inmobixpress.database.query
import com.inmobixpress.model.FeatureHasProperty
import com.inmobixpress.model.Features
import com.inmobixpress.model.FeaturesHasProperty
import com.inmobixpress.model.Properties
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface FeatureHasPropertyDao {
    suspend fun getAll(): List<FeatureHasProperty>
    suspend fun findById(featureId: Int, propertyId: Int): FeatureHasProperty?
    suspend fun insert(featureHasProperty: FeatureHasProperty): Boolean
    suspend fun update(
        featureId: Int,
        propertyId: Int,
        featureHasProperty: FeatureHasProperty
    ): Boolean

    suspend fun delete(featureId: Int, propertyId: Int): Boolean
}

object FeatureHasPropertyDaoImpl : FeatureHasPropertyDao {
    override suspend fun getAll(): List<FeatureHasProperty> = query {
        FeaturesHasProperty.selectAll().map { it.toFeatureHasProperty() }
    }

    override suspend fun findById(featureId: Int, propertyId: Int): FeatureHasProperty? = query {
        FeaturesHasProperty.selectAll().where {
            FeaturesHasProperty.feature eq featureId and (
                    FeaturesHasProperty.property eq propertyId)
        }.map { it.toFeatureHasProperty() }.singleOrNull()
    }

    override suspend fun insert(featureHasProperty: FeatureHasProperty): Boolean = query {
        Features.selectAll().where {
            Features.id eq featureHasProperty.feature.id
        }.map { it.toFeature() }.singleOrNull() ?: return@query false
        Properties.selectAll().where {
            Properties.id eq featureHasProperty.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        FeaturesHasProperty.insert {
            it[feature] = featureHasProperty.feature.id
            it[property] = featureHasProperty.property.id
        }
        return@query true
    }

    override suspend fun update(
        featureId: Int,
        propertyId: Int,
        featureHasProperty: FeatureHasProperty
    ): Boolean = query {
        Features.selectAll().where {
            Features.id eq featureHasProperty.feature.id
        }.map { it.toFeature() }.singleOrNull() ?: return@query false
        Properties.selectAll().where {
            Properties.id eq featureHasProperty.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        FeaturesHasProperty.update({
            FeaturesHasProperty.feature eq featureId and (
                    FeaturesHasProperty.property eq propertyId)
        }) {
            it[feature] = featureHasProperty.feature.id
            it[property] = featureHasProperty.feature.id
        } == 1
    }

    override suspend fun delete(featureId: Int, propertyId: Int): Boolean = query {
        FeaturesHasProperty.deleteWhere {
            feature eq featureId and (property eq propertyId)
        } > 0
    }

    fun ResultRow.toFeatureHasProperty() = FeatureHasProperty(
        feature = findFeature(this[FeaturesHasProperty.feature].value),
        property = findProperty(this[FeaturesHasProperty.property].value)
    )

    private fun findFeature(id: Int) = Features.selectAll().where {
        Features.id eq id
    }.map { it.toFeature() }.single()

    private fun findProperty(id: Int) = Properties.selectAll().where {
        Properties.id eq id
    }.map { it.toProperty() }.single()
}