package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val lastName: String,
    val motherLastName: String,
    val businessName: String,
    val email: String,
    val identityDocument: String,
    val username: String,
    val password: String,
    val documentType: DocumentType,
    val country: Country
)

object Users : IntIdTable(name = "User") {
    val name = varchar(name = "name", length = 100)
    val lastName = varchar(name = "lastname", length = 100)
    val motherLastName = varchar(name = "motherLastName", length = 100)
    val businessName = varchar(name = "businessName", length = 200)
    val email = varchar(name = "email", length = 50)
    val identityDocument = varchar(name = "identityDocument", length = 15)
    val username = varchar(name = "username", length = 60)
    val password = varchar(name = "password", length = 60)
    val documentType = reference(name = "DocumentType_id", foreign = DocumentTypes)
    val country = reference(name = "Country_id", foreign = Countries)
}
