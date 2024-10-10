package com.inmobixpress.repository

import com.inmobixpress.dao.FeatureDao
import com.inmobixpress.dao.FeatureDaoImpl
import com.inmobixpress.model.Feature

class FeatureRepository(
    private val dao: FeatureDao = FeatureDaoImpl
) {
    suspend fun getAll(): List<Feature> = dao.getAll()
    suspend fun findById(id: Int): Feature? = dao.findById(id)
    suspend fun insert(feature: Feature) = dao.insert(feature)
    suspend fun update(id: Int, feature: Feature) = dao.update(id, feature)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}