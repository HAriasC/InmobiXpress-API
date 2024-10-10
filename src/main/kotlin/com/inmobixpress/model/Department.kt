package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Department(
    val id: Int,
    val name: String,
    val country: Country
)

object Departments : IntIdTable(name = "Department") {
    val name = varchar(name = "name", length = 50)
    val country = reference(name = "Country_id", foreign = Countries)
}
