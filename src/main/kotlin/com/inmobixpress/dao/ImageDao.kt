package com.inmobixpress.dao

import com.inmobixpress.dao.PropertyDaoImpl.toProperty
import com.inmobixpress.database.query
import com.inmobixpress.model.Image
import com.inmobixpress.model.Images
import com.inmobixpress.model.Properties
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface ImageDao {
    suspend fun getAll(): List<Image>
    suspend fun findById(id: Int): Image?
    suspend fun insert(image: Image): Boolean
    suspend fun update(id: Int, image: Image): Boolean
    suspend fun delete(id: Int): Boolean
}

object ImageDaoImpl : ImageDao {
    override suspend fun getAll(): List<Image> = query {
        Images.selectAll().map { it.toImage() }
    }

    override suspend fun findById(id: Int): Image? = query {
        Images.selectAll().where {
            Images.id eq id
        }.map { it.toImage() }.singleOrNull()
    }

    override suspend fun insert(image: Image): Boolean = query {
        val propertyId = Properties.selectAll().where {
            Properties.id eq image.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        Images.insert {
            it[uri] = image.uri
            it[property] = propertyId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, image: Image): Boolean = query {
        val propertyId = Properties.selectAll().where {
            Properties.id eq image.property.id
        }.map { it.toProperty() }.singleOrNull() ?: return@query false
        Images.update({ Properties.id eq id }) {
            it[uri] = image.uri
            it[property] = propertyId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Images.deleteWhere {
            Images.id eq id
        } > 0
    }

    fun ResultRow.toImage() = Image(
        id = this[Images.id].value,
        uri = this[Images.uri],
        property = findProperty(id = this[Images.property].value)
    )

    private fun findProperty(id: Int) = Properties.selectAll().where {
        Properties.id eq id
    }.map { it.toProperty() }.single()
}