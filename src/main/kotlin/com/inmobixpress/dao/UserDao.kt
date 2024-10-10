package com.inmobixpress.dao

import com.inmobixpress.dao.CountryDaoImpl.toCountry
import com.inmobixpress.dao.DocumentTypeDaoImpl.toDocumentType
import com.inmobixpress.database.query
import com.inmobixpress.model.Countries
import com.inmobixpress.model.DocumentTypes
import com.inmobixpress.model.User
import com.inmobixpress.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface UserDao {
    suspend fun getAll(): List<User>
    suspend fun findById(id: Int): User?
    suspend fun insert(user: User): Boolean
    suspend fun update(id: Int, user: User): Boolean
    suspend fun delete(id: Int): Boolean
}

object UserDaoImpl : UserDao {
    override suspend fun getAll(): List<User> = query {
        Users.selectAll().map { it.toUser() }
    }

    override suspend fun findById(id: Int): User? = query {
        Users.selectAll().where {
            Users.id eq id
        }.map { it.toUser() }.singleOrNull()
    }

    override suspend fun insert(user: User): Boolean = query {
        val documentTypeId = DocumentTypes.selectAll().where {
            DocumentTypes.id eq user.documentType.id
        }.map { it.toDocumentType() }.singleOrNull() ?: return@query false
        val countryId = Countries.selectAll().where {
            Countries.id eq user.country.id
        }.map { it.toCountry() }.singleOrNull() ?: return@query false
        Users.insert {
            it[name] = user.name
            it[lastName] = user.lastName
            it[motherLastName] = user.motherLastName
            it[businessName] = user.businessName
            it[email] = user.email
            it[identityDocument] = user.identityDocument
            it[username] = user.username
            it[password] = user.password
            it[documentType] = documentTypeId.id
            it[country] = countryId.id
        }
        return@query true
    }

    override suspend fun update(id: Int, user: User): Boolean = query {
        val documentTypeId = DocumentTypes.selectAll().where {
            DocumentTypes.id eq user.documentType.id
        }.map { it.toDocumentType() }.singleOrNull() ?: return@query false
        val countryId = Countries.selectAll().where {
            Countries.id eq user.country.id
        }.map { it.toCountry() }.singleOrNull() ?: return@query false
        Users.update({ Users.id eq id }) {
            it[name] = user.name
            it[lastName] = user.lastName
            it[motherLastName] = user.motherLastName
            it[businessName] = user.businessName
            it[email] = user.email
            it[identityDocument] = user.identityDocument
            it[username] = user.username
            it[password] = user.password
            it[documentType] = documentTypeId.id
            it[country] = countryId.id
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        Users.deleteWhere {
            Users.id eq id
        } > 0
    }

    fun ResultRow.toUser() = User(
        id = this[Users.id].value,
        name = this[Users.name],
        lastName = this[Users.lastName],
        motherLastName = this[Users.motherLastName],
        businessName = this[Users.businessName],
        email = this[Users.email],
        identityDocument = this[Users.identityDocument],
        username = this[Users.username],
        password = this[Users.password],
        documentType = findDocumentType(this[Users.documentType].value),
        country = findCountry(id = this[Users.country].value)
    )

    private fun findDocumentType(id: Int) = DocumentTypes.selectAll().where {
        DocumentTypes.id eq id
    }.map { it.toDocumentType() }.single()

    private fun findCountry(id: Int) = Countries.selectAll().where {
        Countries.id eq id
    }.map { it.toCountry() }.single()
}