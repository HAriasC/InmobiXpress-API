package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.CompositeIdTable

@Serializable
data class UserHasRole(
    val user: User,
    val role: Role
)

object UsersHasRole : CompositeIdTable(name = "UserHasRole") {
    val user = reference(name = "User_id", foreign = Users)
    val role = reference(name = "Role_id", foreign = Roles)

    override val primaryKey = PrimaryKey(user, role)
}
