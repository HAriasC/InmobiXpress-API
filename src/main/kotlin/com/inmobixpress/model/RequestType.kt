package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class RequestType(
    val id: Int,
    val name: String
)

object RequestTypes : IntIdTable(name = "RequestType") {
    val name = varchar(name = "name", length = 50)
}
