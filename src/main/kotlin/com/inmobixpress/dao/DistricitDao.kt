package com.inmobixpress.dao

import com.inmobixpress.dao.LocationDaoImpl.toLocation
import com.inmobixpress.dao.ProvinceDaoImpl.toProvince
import com.inmobixpress.database.query
import com.inmobixpress.model.District
import com.inmobixpress.model.Districts
import com.inmobixpress.model.Locations
import com.inmobixpress.model.Provinces
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface DistrictDao {
    suspend fun getAll(): List<District>
    suspend fun findById(id: Int): District?
    suspend fun insert(district: District): Boolean
    suspend fun update(id: Int, district: District): Boolean
    suspend fun delete(id: Int): Boolean
}

object DistricitDaoImpl : DistrictDao {
    override suspend fun getAll(): List<District> = query {
        Districts.selectAll().map { it.toDistrict() }
    }

    override suspend fun findById(id: Int): District? = query {
        Districts.selectAll().where {
            Districts.id eq id
        }.map { it.toDistrict() }.singleOrNull()
    }

    override suspend fun insert(district: District): Boolean = query {
        val provinceId = Provinces.selectAll().where {
            Provinces.id eq district.province.id
        }.map { it.toProvince() }.singleOrNull() ?: return@query false
        val locationId = Locations.selectAll().where {
            Locations.id eq district.location.id
        }.map { it.toLocation() }.singleOrNull() ?: return@query false
        Districts.insert {
            it[name] = district.name
            it[province] = provinceId.id
            it[location] = locationId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, district: District): Boolean = query {
        val provinceId = Provinces.selectAll().where {
            Provinces.id eq district.province.id
        }.map { it.toProvince() }.singleOrNull() ?: return@query false
        val locationId = Locations.selectAll().where {
            Locations.id eq district.location.id
        }.map { it.toLocation() }.singleOrNull() ?: return@query false
        Districts.update({ Districts.id eq id }) {
            it[name] = district.name
            it[province] = provinceId.id
            it[location] = locationId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Districts.deleteWhere {
            Districts.id eq id
        } > 0
    }

    fun ResultRow.toDistrict() = District(
        id = this[Districts.id].value,
        name = this[Districts.name],
        province = findProvince(id = this[Districts.province].value),
        location = findLocation(id = this[Districts.location].value)
    )

    private fun findProvince(id: Int) = Provinces.selectAll().where {
        Provinces.id eq id
    }.map { it.toProvince() }.single()

    private fun findLocation(id: Int) = Locations.selectAll().where {
        Locations.id eq id
    }.map { it.toLocation() }.single()
}