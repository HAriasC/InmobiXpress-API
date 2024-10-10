package com.inmobixpress.dao

import com.inmobixpress.dao.DepartmentDaoImpl.toDepartment
import com.inmobixpress.database.query
import com.inmobixpress.model.Departments
import com.inmobixpress.model.Province
import com.inmobixpress.model.Provinces
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface ProvinceDao {
    suspend fun getAll(): List<Province>
    suspend fun findById(id: Int): Province?
    suspend fun insert(province: Province): Boolean
    suspend fun update(id: Int, province: Province): Boolean
    suspend fun delete(id: Int): Boolean
}

object ProvinceDaoImpl : ProvinceDao {
    override suspend fun getAll(): List<Province> = query {
        Provinces.selectAll().map { it.toProvince() }
    }

    override suspend fun findById(id: Int): Province? = query {
        Provinces.selectAll().where {
            Provinces.id eq id
        }.map { it.toProvince() }.singleOrNull()
    }

    override suspend fun insert(province: Province): Boolean = query {
        val departmentId = Departments.selectAll().where {
            Departments.id eq province.department.id
        }.map { it.toProvince() }.singleOrNull() ?: return@query false
        Provinces.insert {
            it[name] = province.name
            it[department] = departmentId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, province: Province): Boolean = query {
        val departmentId = Departments.selectAll().where {
            Departments.id eq province.department.id
        }.map { it.toProvince() }.singleOrNull() ?: return@query false
        Departments.update({ Departments.id eq id }) {
            it[name] = province.name
            it[country] = departmentId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Provinces.deleteWhere {
            Provinces.id eq id
        } > 0
    }

    fun ResultRow.toProvince() = Province(
        id = this[Provinces.id].value,
        name = this[Provinces.name],
        department = findDepartment(id = this[Provinces.department].value)
    )

    private fun findDepartment(id: Int) = Departments.selectAll().where {
        Departments.id eq id
    }.map { it.toDepartment() }.single()
}