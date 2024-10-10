package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Device(
    val id: Int,
    val phone: String,
    val token: String,
    val user: User
)

object Devices : IntIdTable(name = "Device") {
    val phone = varchar(name = "phone", length = 45)
    val token = varchar(name = "token", length = 500)
    val user = reference(name = "User_id", foreign = Users)
}