package com.inmobixpress.repository

import com.inmobixpress.dao.OfferTypeDao
import com.inmobixpress.dao.OfferTypeDaoImpl
import com.inmobixpress.model.OfferType

class OfferTypeRepository(
    private val dao: OfferTypeDao = OfferTypeDaoImpl
) {
    suspend fun getAll(): List<OfferType> = dao.getAll()
    suspend fun findById(id: Int): OfferType? = dao.findById(id)
    suspend fun insert(offerType: OfferType) = dao.insert(offerType)
    suspend fun update(id: Int, offerType: OfferType) = dao.update(id, offerType)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}