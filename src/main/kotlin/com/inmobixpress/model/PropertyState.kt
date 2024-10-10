package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class PropertyState(
    val id: Int,
    val name: String
)

object PropertyStates : IntIdTable(name = "PropertyState") {
    val name = varchar(name = "name", length = 50)
}
