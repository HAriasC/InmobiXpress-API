package com.inmobixpress.dao

import com.inmobixpress.dao.CountryDaoImpl.toCountry
import com.inmobixpress.database.query
import com.inmobixpress.model.Countries
import com.inmobixpress.model.Department
import com.inmobixpress.model.Departments
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface DepartmentDao {
    suspend fun getAll(): List<Department>
    suspend fun findById(id: Int): Department?
    suspend fun insert(department: Department): Boolean
    suspend fun update(id: Int, department: Department): Boolean
    suspend fun delete(id: Int): Boolean
}

object DepartmentDaoImpl : DepartmentDao {
    override suspend fun getAll(): List<Department> = query {
        Departments.selectAll().map { it.toDepartment() }
    }

    override suspend fun findById(id: Int): Department? = query {
        Departments.selectAll().where {
            Departments.id eq id
        }.map { it.toDepartment() }.singleOrNull()
    }

    override suspend fun insert(department: Department): Boolean = query {
        val countryId = Countries.selectAll().where {
            Countries.id eq department.country.id
        }.map { it.toCountry() }.singleOrNull() ?: return@query false
        Departments.insert {
            it[name] = department.name
            it[country] = countryId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, department: Department): Boolean = query {
        val countryId = Countries.selectAll().where {
            Countries.id eq department.country.id
        }.map { it.toCountry() }.singleOrNull() ?: return@query false
        Departments.update({ Departments.id eq id }) {
            it[name] = department.name
            it[country] = countryId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Departments.deleteWhere {
            Departments.id eq id
        } > 0
    }

    fun ResultRow.toDepartment() = Department(
        id = this[Departments.id].value,
        name = this[Departments.name],
        country = findCountry(id = this[Departments.country].value)
    )

    private fun findCountry(id: Int) = Countries.selectAll().where {
        Countries.id eq id
    }.map { it.toCountry() }.single()
}