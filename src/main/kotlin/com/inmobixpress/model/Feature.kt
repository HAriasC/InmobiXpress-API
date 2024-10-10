package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Feature(
    val id: Int,
    val name: String,
    val priority: Boolean
)

object Features : IntIdTable(name = "Feature") {
    val name = varchar(name = "name", length = 50)
    val priority = bool(name = "priority")
}
