package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class District(
    val id: Int,
    val name: String,
    val province: Province,
    val location: Location
)

object Districts : IntIdTable(name = "District") {
    val name = varchar(name = "name", length = 50)
    val province = reference(name = "Province_id", foreign = Provinces)
    val location = reference(name = "Location_id", foreign = Locations)
}
