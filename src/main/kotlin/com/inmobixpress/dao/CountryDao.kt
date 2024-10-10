package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.Countries
import com.inmobixpress.model.Country
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface CountryDao {
    suspend fun getAll(): List<Country>
    suspend fun findById(id: Int): Country?
    suspend fun insert(country: Country)
    suspend fun update(id: Int, country: Country): Boolean
    suspend fun delete(id: Int): Boolean
}

object CountryDaoImpl : CountryDao {
    override suspend fun getAll(): List<Country> = query {
        Countries.selectAll().map { it.toCountry() }
    }

    override suspend fun findById(id: Int): Country? = query {
        Countries.selectAll().where {
            Countries.id eq id
        }.map { it.toCountry() }.singleOrNull()
    }

    override suspend fun insert(country: Country): Unit = query {
        Countries.insert {
            it[name] = country.name
            it[countryCode] = country.countryCode
        }
    }

    override suspend fun update(id: Int, country: Country): Boolean = query {
        Countries.update({ Countries.id eq id }) {
            it[name] = country.name
            it[countryCode] = country.countryCode
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Countries.deleteWhere {
            Countries.id eq id
        } > 0
    }

    fun ResultRow.toCountry() = Country(
        id = this[Countries.id].value,
        name = this[Countries.name],
        countryCode = this[Countries.countryCode]
    )
}