package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Image(
    val id: Int,
    val uri: String,
    val property: Property
)

object Images : IntIdTable(name = "Image") {
    val uri = varchar(name = "uri", length = 1000)
    val property = reference(name = "Property_id", foreign = Properties)
}
