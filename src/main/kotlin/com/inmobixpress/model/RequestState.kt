package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class RequestState(
    val id: Int,
    val name: String
)

object RequestStates : IntIdTable(name = "RequestState") {
    val name = varchar(name = "name", length = 45)
}
