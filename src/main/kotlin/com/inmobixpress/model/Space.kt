package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Space(
    val id: Int,
    val name: String
)

object Spaces : IntIdTable(name = "Space") {
    val name = varchar(name = "name", length = 60)
}
