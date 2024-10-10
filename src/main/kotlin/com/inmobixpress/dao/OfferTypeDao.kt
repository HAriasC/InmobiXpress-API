package com.inmobixpress.dao

import com.inmobixpress.database.query
import com.inmobixpress.model.OfferType
import com.inmobixpress.model.OfferTypes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface OfferTypeDao {
    suspend fun getAll(): List<OfferType>
    suspend fun findById(id: Int): OfferType?
    suspend fun insert(offerType: OfferType)
    suspend fun update(id: Int, offerType: OfferType): Boolean
    suspend fun delete(id: Int): Boolean
}

object OfferTypeDaoImpl : OfferTypeDao {
    override suspend fun getAll(): List<OfferType> = query {
        OfferTypes.selectAll().map { it.toOfferType() }
    }

    override suspend fun findById(id: Int): OfferType? = query {
        OfferTypes.selectAll().where {
            OfferTypes.id eq id
        }.map { it.toOfferType() }.singleOrNull()
    }

    override suspend fun insert(offerType: OfferType): Unit = query {
        OfferTypes.insert {
            it[name] = offerType.name
        }
    }

    override suspend fun update(id: Int, offerType: OfferType): Boolean = query {
        OfferTypes.update({ OfferTypes.id eq id }) {
            it[name] = offerType.name
        } == 1
    }

    override suspend fun delete(id: Int): Boolean = query {
        OfferTypes.deleteWhere {
            OfferTypes.id eq id
        } > 0
    }

    fun ResultRow.toOfferType() = OfferType(
        id = this[OfferTypes.id].value,
        name = this[OfferTypes.name]
    )
}