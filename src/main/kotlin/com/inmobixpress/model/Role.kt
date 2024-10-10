package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Role(
    val id: Int,
    val name: String
)

object Roles : IntIdTable(name = "Role") {
    val name = varchar(name = "name", length = 60)
}
