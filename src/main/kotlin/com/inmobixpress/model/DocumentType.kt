package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class DocumentType(
    val id: Int,
    val name: String
)

object DocumentTypes : IntIdTable(name = "DocumentType") {
    val name = varchar(name = "name", length = 50)
}
