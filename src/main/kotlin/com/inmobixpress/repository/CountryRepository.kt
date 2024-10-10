package com.inmobixpress.repository

import com.inmobixpress.dao.CountryDao
import com.inmobixpress.dao.CountryDaoImpl
import com.inmobixpress.model.Country

class CountryRepository(
    private val dao: CountryDao = CountryDaoImpl
) {
    suspend fun getAll(): List<Country> = dao.getAll()
    suspend fun findById(id: Int): Country? = dao.findById(id)
    suspend fun insert(country: Country) = dao.insert(country)
    suspend fun update(id: Int, country: Country) = dao.update(id, country)
    suspend fun delete(id: Int): Boolean = dao.delete(id)
}