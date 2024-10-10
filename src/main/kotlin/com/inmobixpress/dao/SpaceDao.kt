package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.Space
import com.inmobixpress.model.Spaces
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface SpaceDao {
    suspend fun getAll(): List<Space>
    suspend fun findById(id: Int): Space?
    suspend fun insert(space: Space)
    suspend fun update(id: Int, space: Space): Boolean
    suspend fun delete(id: Int): Boolean
}

object SpaceDaoImpl : SpaceDao {
    override suspend fun getAll(): List<Space> = query {
        Spaces.selectAll().map { it.toSpace() }
    }

    override suspend fun findById(id: Int): Space? = query {
        Spaces.selectAll().where {
            Spaces.id eq id
        }.map { it.toSpace() }.singleOrNull()
    }

    override suspend fun insert(space: Space): Unit = query {
        Spaces.insert {
            it[name] = space.name
        }
    }

    override suspend fun update(id: Int, space: Space): Boolean = query {
        Spaces.update({ Spaces.id eq id }) {
            it[name] = space.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Spaces.deleteWhere {
            Spaces.id eq id
        } > 0
    }

    fun ResultRow.toSpace() = Space(
        id = this[Spaces.id].value,
        name = this[Spaces.name]
    )
}