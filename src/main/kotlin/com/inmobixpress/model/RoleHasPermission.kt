package com.inmobixpress.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.CompositeIdTable

@Serializable
data class RoleHasPermission(
    val role: Role,
    val space: Space,
    val permission: Permission
)

object RolesHasPermission : CompositeIdTable(name = "RoleHasPermission") {
    val role = reference(name = "Role_id", foreign = Roles)
    val space = reference(name = "Space_id", foreign = Spaces)
    val permission = reference(name = "Permission_id", foreign = Permissions)

    override val primaryKey = PrimaryKey(role, space, permission)
}