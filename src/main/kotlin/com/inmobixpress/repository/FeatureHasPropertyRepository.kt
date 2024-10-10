package com.inmobixpress.repository

import com.inmobixpress.dao.FeatureHasPropertyDao
import com.inmobixpress.dao.FeatureHasPropertyDaoImpl
import com.inmobixpress.model.FeatureHasProperty

class FeatureHasPropertyRepository(
    private val dao: FeatureHasPropertyDao = FeatureHasPropertyDaoImpl
) {
    suspend fun getAll(): List<FeatureHasProperty> = dao.getAll()
    suspend fun findById(featureId: Int, propertyId: Int): FeatureHasProperty? =
        dao.findById(featureId, propertyId)

    suspend fun insert(featureHasProperty: FeatureHasProperty) = dao.insert(featureHasProperty)
    suspend fun update(featureId: Int, propertyId: Int, featureHasProperty: FeatureHasProperty) =
        dao.update(featureId, propertyId, featureHasProperty)

    suspend fun delete(featureId: Int, propertyId: Int): Boolean = dao.delete(featureId, propertyId)
}