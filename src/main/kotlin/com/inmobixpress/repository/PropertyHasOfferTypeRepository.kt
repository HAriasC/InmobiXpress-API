package com.inmobixpress.repository

import com.inmobixpress.dao.PropertyHasOfferTypeDao
import com.inmobixpress.dao.PropertyHasOfferTypeDaoImpl
import com.inmobixpress.model.PropertyHasOfferType

class PropertyHasOfferTypeRepository(
    private val dao: PropertyHasOfferTypeDao = PropertyHasOfferTypeDaoImpl
) {
    suspend fun getAll(): List<PropertyHasOfferType> = dao.getAll()
    suspend fun findById(propertyId: Int, offerTypeId: Int): PropertyHasOfferType? =
        dao.findById(propertyId, offerTypeId)

    suspend fun insert(propertyHasOfferType: PropertyHasOfferType) =
        dao.insert(propertyHasOfferType)

    suspend fun update(
        propertyId: Int,
        offerTypeId: Int,
        propertyHasOfferType: PropertyHasOfferType
    ) =
        dao.update(propertyId, offerTypeId, propertyHasOfferType)

    suspend fun delete(propertyId: Int, offerTypeId: Int): Boolean =
        dao.delete(propertyId, offerTypeId)
}