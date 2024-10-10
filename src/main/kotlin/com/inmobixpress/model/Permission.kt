package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Permission(
    val id: Int,
    val name: String
)

object Permissions : IntIdTable(name = "Permission") {
    val name = varchar(name = "name", length = 60)
}
