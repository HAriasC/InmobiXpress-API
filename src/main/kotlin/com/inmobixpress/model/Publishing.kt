package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Publishing(
    val id: Int,
    val numberView: Int,
    val property: Property
)

object Publishings : IntIdTable(name = "Publishing") {
    val numberView = integer(name = "numberView")
    val property = reference(name = "Property_id", foreign = Properties)
}
