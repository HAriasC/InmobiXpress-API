package com.inmobixpress.dao

import com.inmobixpress.dao.OfferTypeDaoImpl.toOfferType
import com.inmobixpress.dao.PropertyDaoImpl.toProperty
import com.inmobixpress.database.query
import com.inmobixpress.model.OfferTypes
import com.inmobixpress.model.Properties
import com.inmobixpress.model.PropertiesHasOfferType
import com.inmobixpress.model.PropertyHasOfferType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface PropertyHasOfferTypeDao {
    suspend fun getAll(): List<PropertyHasOfferType>
    suspend fun findById(propertyId: Int, offerTypeId: Int): PropertyHasOfferType?
    suspend fun insert(propertyHasOfferType: PropertyHasOfferType): Boolean
    suspend fun update(
        propertyId: Int,
        offerTypeId: Int,
        propertyHasOfferType: PropertyHasOfferType
    ): Boolean

    suspend fun delete(propertyId: Int, offerTypeId: Int): Boolean
}

object PropertyHasOfferTypeDaoImpl : PropertyHasOfferTypeDao {
    override suspend fun getAll(): List<PropertyHasOfferType> = query {
        PropertiesHasOfferType.selectAll().map { it.toPropertyHasOfferType() }
    }

    override suspend fun findById(
        propertyId: Int,
        offerTypeId: Int
    ): PropertyHasOfferType? = query {
        PropertiesHasOfferType.selectAll().where {
            PropertiesHasOfferType.property eq propertyId and (
                    PropertiesHasOfferType.offerType eq offerTypeId)
        }.map { it.toPropertyHasOfferType() }.singleOrNull()
    }

    override suspend fun insert(propertyHasOfferType: PropertyHasOfferType): Boolean = query {
        Properties.selectAll().where {
            Properties.id eq propertyHasOfferType.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        OfferTypes.selectAll().where {
            OfferTypes.id eq propertyHasOfferType.offerType.id
        }.map { it.toOfferType() }.singleOrNull() ?: return@query false
        PropertiesHasOfferType.insert {
            it[property] = propertyHasOfferType.property.id
            it[offerType] = propertyHasOfferType.offerType.id
            it[price] = propertyHasOfferType.price
        }
        return@query true
    }

    override suspend fun update(
        propertyId: Int,
        offerTypeId: Int,
        propertyHasOfferType: PropertyHasOfferType
    ): Boolean = query {
        Properties.selectAll().where {
            Properties.id eq propertyHasOfferType.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        OfferTypes.selectAll().where {
            OfferTypes.id eq propertyHasOfferType.offerType.id
        }.map { it.toOfferType() }.singleOrNull() ?: return@query false
        PropertiesHasOfferType.update({
            PropertiesHasOfferType.property eq propertyId and (
                    PropertiesHasOfferType.offerType eq offerTypeId)
        }) {
            it[property] = propertyHasOfferType.property.id
            it[offerType] = propertyHasOfferType.offerType.id
            it[price] = propertyHasOfferType.price
        } == 1
    }

    override suspend fun delete(propertyId: Int, offerTypeId: Int): Boolean = query {
        PropertiesHasOfferType.deleteWhere {
            property eq propertyId and (offerType eq offerTypeId)
        } > 0
    }

    fun ResultRow.toPropertyHasOfferType() = PropertyHasOfferType(
        property = findProperty(this[PropertiesHasOfferType.property].value),
        offerType = findOfferType(this[PropertiesHasOfferType.offerType].value),
        price = this[PropertiesHasOfferType.price]
    )

    private fun findProperty(id: Int) = Properties.selectAll().where {
        Properties.id eq id
    }.map { it.toProperty() }.single()

    private fun findOfferType(id: Int) = OfferTypes.selectAll().where {
        OfferTypes.id eq id
    }.map { it.toOfferType() }.single()
}