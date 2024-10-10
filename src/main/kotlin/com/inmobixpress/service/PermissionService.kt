package com.inmobixpress.service

import com.inmobixpress.model.Permission
import com.inmobixpress.repository.PermissionRepository
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

fun Route.permissionService() {
    val repository = PermissionRepository()
    get(path = "/permission") {
        val permissions = repository.getAll()
        if (permissions.isNotEmpty()) {
            call.respond(message = permissions)
        } else {
            call.respondText(
                text = "No permissions found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/permission/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val permission = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No permission with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = permission)
    }
    post(path = "/permission") {
        val permission = call.receiveNullable<Permission>()
            ?: return@post call.respondText(
                text = "No permission body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(permission.toString())
        repository.insert(permission = permission)
        call.respondText(
            text = "Permission stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/permission/{id?}") {
        val permission = call.receiveNullable<Permission>()
            ?: return@put call.respondText(
                text = "No permission body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(permission.toString())
        if (repository.update(id = id.toInt(), permission = permission)) {
            call.respondText(
                text = "Permission update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No permission stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/permission/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Permission removed correctly",
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