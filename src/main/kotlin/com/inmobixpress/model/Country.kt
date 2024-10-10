package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Country(
    val id: Int,
    val name: String,
    val countryCode: String
)

object Countries : IntIdTable(name = "Country") {
    val name = varchar(name = "name", length = 50)
    val countryCode = varchar(name = "countryCode", length = 10)
}
