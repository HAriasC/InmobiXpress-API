package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption

@Serializable
data class FeatureHasProperty(
    val feature: Feature,
    val property: Property
)

object FeaturesHasProperty : CompositeIdTable(name = "FeatureHasProperty") {
    val feature = reference(name = "Feature_id", foreign = Features)
    val property = reference(
        name = "Property_id",
        foreign = Properties,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    override val primaryKey = PrimaryKey(feature, property)
}
