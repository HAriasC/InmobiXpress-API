package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Province(
    val id: Int,
    val name: String,
    val department: Department
)

object Provinces : IntIdTable(name = "Province") {
    val name = varchar(name = "name", length = 50)
    val department = reference(name = "Department_id", foreign = Departments)
}
