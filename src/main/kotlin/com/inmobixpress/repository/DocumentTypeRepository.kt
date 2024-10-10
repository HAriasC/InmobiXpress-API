package com.inmobixpress.repository

import com.inmobixpress.dao.DocumentTypeDao
import com.inmobixpress.dao.DocumentTypeDaoImpl
import com.inmobixpress.model.DocumentType

class DocumentTypeRepository(
    private val dao: DocumentTypeDao = DocumentTypeDaoImpl
) {
    suspend fun getAll(): List<DocumentType> = dao.getAll()
    suspend fun findById(id: Int): DocumentType? = dao.findById(id)
    suspend fun insert(documentType: DocumentType) = dao.insert(documentType)
    suspend fun update(id: Int, documentType: DocumentType) = dao.update(id, documentType)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}