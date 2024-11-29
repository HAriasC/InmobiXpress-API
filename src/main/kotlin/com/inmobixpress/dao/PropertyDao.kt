package com.inmobixpress.dao

import com.inmobixpress.dao.DistricitDaoImpl.toDistrict
import com.inmobixpress.dao.LocationDaoImpl.toLocation
import com.inmobixpress.dao.PropertyStateDaoImpl.toPropertyState
import com.inmobixpress.dao.PropertyTypeDaoImpl.toPropertyType
import com.inmobixpress.dao.UserDaoImpl.toUser
import com.inmobixpress.database.query
import com.inmobixpress.model.Districts
import com.inmobixpress.model.Locations
import com.inmobixpress.model.Properties
import com.inmobixpress.model.Property
import com.inmobixpress.model.PropertyStates
import com.inmobixpress.model.PropertyTypes
import com.inmobixpress.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface PropertyDao {
    suspend fun getAll(): List<Property>
    suspend fun findById(id: Int): Property?
    suspend fun insert(property: Property): Boolean
    suspend fun update(id: Int, property: Property): Boolean
    suspend fun delete(id: Int): Boolean
}

object PropertyDaoImpl : PropertyDao {
    override suspend fun getAll(): List<Property> = query {
        Properties.selectAll().map { it.toProperty() }
    }

    override suspend fun findById(id: Int): Property? = query {
        Properties.selectAll().where {
            Properties.id eq id
        }.map { it.toProperty() }.singleOrNull()
    }

    override suspend fun insert(property: Property): Boolean = query {
        val propertyTypeId = PropertyTypes.selectAll().where {
            PropertyTypes.id eq property.propertyType.id
        }.map { it.toPropertyType() }.singleOrNull() ?: return@query false
        val propertyStateId = PropertyStates.selectAll().where {
            PropertyStates.id eq property.propertyState.id
        }.map { it.toPropertyState() }.singleOrNull() ?: return@query false
        val locationId = Locations.selectAll().where {
            Locations.id eq property.location.id
        }.map { it.toLocation() }.singleOrNull() ?: return@query false
        val districtId = PropertyTypes.selectAll().where {
            Districts.id eq property.district.id
        }.map { it.toDistrict() }.singleOrNull() ?: return@query false
        val userId = Users.selectAll().where {
            Users.id eq property.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Properties.insert {
            it[title] = property.title
            it[description] = property.description
            it[maintenance] = property.maintenance
            it[address] = property.address
            it[postalCode] = property.postalCode
            it[nBedroom] = property.nBedroom
            it[nBathroom] = property.nBathroom
            it[nGarage] = property.nGarage
            it[buildingYear] = property.buildingYear
            it[floor] = property.floor
            it[totalArea] = property.totalArea
            it[builtArea] = property.builtArea
            it[propertyType] = propertyTypeId.id
            it[propertyState] = propertyStateId.id
            it[location] = locationId.id
            it[district] = districtId.id
            it[user] = userId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, property: Property): Boolean = query {
        val propertyTypeId = PropertyTypes.selectAll().where {
            PropertyTypes.id eq property.propertyType.id
        }.map { it.toPropertyType() }.singleOrNull() ?: return@query false
        val propertyStateId = PropertyStates.selectAll().where {
            PropertyStates.id eq property.propertyState.id
        }.map { it.toPropertyState() }.singleOrNull() ?: return@query false
        val locationId = Locations.selectAll().where {
            Locations.id eq property.location.id
        }.map { it.toLocation() }.singleOrNull() ?: return@query false
        val districtId = PropertyTypes.selectAll().where {
            Districts.id eq property.district.id
        }.map { it.toDistrict() }.singleOrNull() ?: return@query false
        val userId = Users.selectAll().where {
            Users.id eq property.user.id
        }.map { it.toUser() }.singleOrNull() ?: return@query false
        Properties.update({ Properties.id eq id }) {
            it[title] = property.title
            it[description] = property.description
            it[maintenance] = property.maintenance
            it[address] = property.address
            it[postalCode] = property.postalCode
            it[nBedroom] = property.nBedroom
            it[nBathroom] = property.nBathroom
            it[nGarage] = property.nGarage
            it[buildingYear] = property.buildingYear
            it[floor] = property.floor
            it[totalArea] = property.totalArea
            it[builtArea] = property.builtArea
            it[propertyType] = propertyTypeId.id
            it[propertyState] = propertyStateId.id
            it[location] = locationId.id
            it[district] = districtId.id
            it[user] = userId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Properties.deleteWhere {
            Properties.id eq id
        } > 0
    }

    fun ResultRow.toProperty() = Property(
        id = this[Properties.id].value,
        title = this[Properties.title],
        description = this[Properties.description],
        maintenance = this[Properties.maintenance],
        address = this[Properties.address],
        postalCode = this[Properties.postalCode],
        nBedroom = this[Properties.nBedroom],
        nBathroom = this[Properties.nBathroom],
        nGarage = this[Properties.nGarage],
        buildingYear = this[Properties.buildingYear],
        floor = this[Properties.floor],
        totalArea = this[Properties.totalArea],
        builtArea = this[Properties.builtArea],
        propertyType = findPropertyType(id = this[Properties.propertyType].value),
        propertyState = findPropertyState(id = this[Properties.propertyState].value),
        location = findLocation(id = this[Properties.location].value),
        district = findDistrict(id = this[Properties.district].value),
        user = findUser(id = this[Properties.user].value)
    )

    private fun findPropertyType(id: Int) = PropertyTypes.selectAll().where {
        PropertyTypes.id eq id
    }.map { it.toPropertyType() }.single()

    private fun findPropertyState(id: Int) = PropertyStates.selectAll().where {
        PropertyStates.id eq id
    }.map { it.toPropertyState() }.single()

    private fun findLocation(id: Int) = Locations.selectAll().where {
        Locations.id eq id
    }.map { it.toLocation() }.single()

    private fun findDistrict(id: Int) = Districts.selectAll().where {
        Districts.id eq id
    }.map { it.toDistrict() }.single()

    private fun findUser(id: Int) = Users.selectAll().where {
        Users.id eq id
    }.map { it.toUser() }.single()
}