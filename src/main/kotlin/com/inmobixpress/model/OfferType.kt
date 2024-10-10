package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class OfferType(
    val id: Int,
    val name: String
)

object OfferTypes : IntIdTable(name = "OfferType") {
    val name = varchar(name = "name", length = 50)
}
