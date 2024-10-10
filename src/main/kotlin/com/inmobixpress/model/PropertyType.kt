package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class PropertyType(
    val id: Int,
    val name: String
)

object PropertyTypes : IntIdTable(name = "PropertyType") {
    val name = varchar(name = "name", length = 50)
}
