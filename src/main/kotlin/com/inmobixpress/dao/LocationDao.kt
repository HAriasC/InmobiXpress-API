package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.Location
import com.inmobixpress.model.Locations
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface LocationDao {
    suspend fun getAll(): List<Location>
    suspend fun findById(id: Int): Location?
    suspend fun insert(location: Location)
    suspend fun update(id: Int, location: Location): Boolean
    suspend fun delete(id: Int): Boolean
}

object LocationDaoImpl : LocationDao {
    override suspend fun getAll(): List<Location> = query {
        Locations.selectAll().map { it.toLocation() }
    }

    override suspend fun findById(id: Int): Location? = query {
        Locations.selectAll().where {
            Locations.id eq id
        }.map { it.toLocation() }.singleOrNull()
    }

    override suspend fun insert(location: Location): Unit = query {
        Locations.insert {
            it[latitude] = location.latitude
            it[longitude] = location.longitude
            it[altitude] = location.altitude
            it[altitudeBase] = location.altitudeBase
        }
    }

    override suspend fun update(id: Int, location: Location): Boolean = query {
        Locations.update({ Locations.id eq id }) {
            it[latitude] = location.latitude
            it[longitude] = location.longitude
            it[altitude] = location.altitude
            it[altitudeBase] = location.altitudeBase
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Locations.deleteWhere {
            Locations.id eq id
        } > 0
    }

    fun ResultRow.toLocation() = Location(
        id = this[Locations.id].value,
        latitude = this[Locations.latitude],
        longitude = this[Locations.longitude],
        altitude = this[Locations.altitude],
        altitudeBase = this[Locations.altitudeBase]
    )
}