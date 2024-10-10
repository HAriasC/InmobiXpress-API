package com.inmobixpress.service

import com.inmobixpress.model.RoleHasPermission
import com.inmobixpress.repository.RoleHasPermissionRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.log
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.roleHasPermissionService() {
    val repository = RoleHasPermissionRepository()
    get(path = "/roleHasPermission") {
        val rolesHasPermission = repository.getAll()
        if (rolesHasPermission.isNotEmpty()) {
            call.respond(message = rolesHasPermission)
        } else {
            call.respondText(
                text = "No roles x space x permission found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/roleHasPermission/{roleId?}/{spaceId?}/{permissionId?}") {
        val roleId = call.parameters["roleId"]
            ?: return@get call.respondText(
                text = "Missing role id",
                status = HttpStatusCode.BadRequest
            )
        val spaceId = call.parameters["spaceId"]
            ?: return@get call.respondText(
                text = "Missing space id",
                status = HttpStatusCode.BadRequest
            )
        val permissionId = call.parameters["permissionId"]
            ?: return@get call.respondText(
                text = "Missing permission id",
                status = HttpStatusCode.BadRequest
            )
        val roleHasPermission = repository.findById(
            roleId = roleId.toInt(),
            spaceId = spaceId.toInt(),
            permissionId = permissionId.toInt()
        ) ?: return@get call.respondText(
            text = "No role x space x permission with " +
                    "rId: $roleId, sId: $spaceId or $permissionId found",
            status = HttpStatusCode.NotFound
        )
        call.respond(message = roleHasPermission)
    }
    post(path = "/roleHasPermission") {
        val roleHasPermission = call.receiveNullable<RoleHasPermission>()
            ?: return@post call.respondText(
                text = "No role x space x permission body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(roleHasPermission.toString())
        if (repository.insert(roleHasPermission = roleHasPermission)) {
            call.respondText(
                text = "Role x space x permission stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Role, space or permission not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/roleHasPermission/{roleId?}/{spaceId?}/{permissionId?}") {
        val roleHasPermission = call.receiveNullable<RoleHasPermission>()
            ?: return@put call.respondText(
                text = "No role x space x permission body",
                status = HttpStatusCode.BadRequest
            )
        val roleId = call.parameters["roleId"]
            ?: return@put call.respondText(
                text = "Missing publishing state id",
                status = HttpStatusCode.BadRequest
            )
        val spaceId = call.parameters["spaceId"]
            ?: return@put call.respondText(
                text = "Missing space id",
                status = HttpStatusCode.BadRequest
            )
        val permissionId = call.parameters["permissionId"]
            ?: return@put call.respondText(
                text = "Missing permission id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(roleHasPermission.toString())
        if (repository.update(
                roleId = roleId.toInt(),
                spaceId = spaceId.toInt(),
                permissionId = permissionId.toInt(),
                roleHasPermission = roleHasPermission
            )
        ) {
            call.respondText(
                text = "Role x space x permission update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No role x space x permission stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/roleHasPermission/{roleId?}/{spaceId?}/{permissionId?}") {
        val roleId = call.parameters["publishingStateId"]
            ?: return@delete call.respondText(
                text = "Missing role id",
                status = HttpStatusCode.BadRequest
            )
        val spaceId = call.parameters["spaceId"]
            ?: return@delete call.respondText(
                text = "Missing space id",
                status = HttpStatusCode.BadRequest
            )
        val permissionId = call.parameters["permissionId"]
            ?: return@delete call.respondText(
                text = "Missing permission id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(
                roleId = roleId.toInt(),
                spaceId = spaceId.toInt(),
                permissionId = permissionId.toInt()
            )
        ) {
            call.respondText(
                text = "Role x space x permission removed correctly",
                status = HttpStatusCode.Accepted
            )
        } else {
            call.respondText(
                text = "Not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
}