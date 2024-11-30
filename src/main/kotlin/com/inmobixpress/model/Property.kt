package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Property(
    val id: Int,
    val title: String,
    val description: String,
    val maintenance: Double,
    val address: String,
    val postalCode: String,
    val nBedroom: Int,
    val nBathroom: Double,
    val nGarage: Int,
    val buildingYear: Int,
    val floor: Int,
    val totalArea: Double,
    val builtArea: Double,
    val propertyType: PropertyType,
    val propertyState: PropertyState,
    var location: Location,
    val district: District,
    val user: User
)

object Properties : IntIdTable(name = "Property") {
    val title = varchar(name = "title", length = 50)
    val description = largeText(name = "description")
    val maintenance = double(name = "maintenance")
    val address = varchar(name = "address", length = 80)
    val postalCode = varchar(name = "postalCode", length = 10)
    val nBedroom = integer(name = "nBedroom")
    val nBathroom = double(name = "nBathroom")
    val nGarage = integer(name = "nGarage")
    val buildingYear = integer(name = "buildingYear")
    val floor = integer(name = "floor")
    val totalArea = double(name = "totalArea")
    val builtArea = double(name = "builtArea")
    val propertyType = reference(name = "PropertyType_id", foreign = PropertyTypes)
    val propertyState = reference(name = "PropertyState_id", foreign = PropertyStates)
    val location = reference(name = "Location_id", foreign = Locations)
    val district = reference(name = "District_id", foreign = Districts)
    val user = reference(name = "User_id", foreign = Users)
}
