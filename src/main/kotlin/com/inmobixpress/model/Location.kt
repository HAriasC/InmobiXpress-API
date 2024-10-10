package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Location(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val altitudeBase: Double
)

object Locations : IntIdTable(name = "Location") {
    val latitude = double(name = "latitude")
    val longitude = double(name = "longitude")
    val altitude = double(name = "altitude")
    val altitudeBase = double(name = "altitudeBase")
}
