package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.DocumentType
import com.inmobixpress.model.DocumentTypes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface DocumentTypeDao {
    suspend fun getAll(): List<DocumentType>
    suspend fun findById(id: Int): DocumentType?
    suspend fun insert(documentType: DocumentType)
    suspend fun update(id: Int, documentType: DocumentType): Boolean
    suspend fun delete(id: Int): Boolean
}

object DocumentTypeDaoImpl : DocumentTypeDao {
    override suspend fun getAll(): List<DocumentType> = query {
        DocumentTypes.selectAll().map { it.toDocumentType() }
    }

    override suspend fun findById(id: Int): DocumentType? = query {
        DocumentTypes.selectAll().where {
            DocumentTypes.id eq id
        }.map { it.toDocumentType() }.singleOrNull()
    }

    override suspend fun insert(documentType: DocumentType): Unit = query {
        DocumentTypes.insert {
            it[name] = documentType.name
        }
    }

    override suspend fun update(id: Int, documentType: DocumentType): Boolean = query {
        DocumentTypes.update({ DocumentTypes.id eq id }) {
            it[name] = documentType.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        DocumentTypes.deleteWhere {
            DocumentTypes.id eq id
        } > 0
    }

    fun ResultRow.toDocumentType() = DocumentType(
        id = this[DocumentTypes.id].value,
        name = this[DocumentTypes.name]
    )
}