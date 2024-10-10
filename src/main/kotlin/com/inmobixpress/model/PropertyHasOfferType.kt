package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

@Serializable
data class PropertyHasOfferType(
    val property: Property,
    val offerType: OfferType,
    val price: Double
)

object PropertiesHasOfferType : CompositeIdTable(name = "PropertyHasOfferType") {
    val property = reference(name = "Property_id", foreign = Properties)
    val offerType = reference(
        name = "OfferType_id",
        foreign = OfferTypes,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val price = double(name = "price")

    override val primaryKey = PrimaryKey(property, offerType)
}
