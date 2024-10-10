package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class PublishingState(
    val id: Int,
    val name: String
)

object PublishingStates : IntIdTable(name = "PublishingState") {
    val name = varchar(name = "name", length = 50)
}
