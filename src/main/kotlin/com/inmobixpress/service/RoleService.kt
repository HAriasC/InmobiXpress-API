package com.inmobixpress.service

import com.inmobixpress.model.Role
import com.inmobixpress.repository.RoleRepository
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

fun Route.roleService() {
    val repository = RoleRepository()
    get(path = "/role") {
        val roles = repository.getAll()
        if (roles.isNotEmpty()) {
            call.respond(message = roles)
        } else {
            call.respondText(
                text = "No roles found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/role/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val role = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No role with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = role)
    }
    post(path = "/role") {
        val role = call.receiveNullable<Role>()
            ?: return@post call.respondText(
                text = "No role body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(role.toString())
        repository.insert(role = role)
        call.respondText(
            text = "Role stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/role/{id?}") {
        val role = call.receiveNullable<Role>()
            ?: return@put call.respondText(
                text = "No role body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(role.toString())
        if (repository.update(id = id.toInt(), role = role)) {
            call.respondText(
                text = "Role update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No role stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/role/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Role removed correctly",
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