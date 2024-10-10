package com.inmobixpress.service

import com.inmobixpress.model.UserHasRole
import com.inmobixpress.repository.UserHasRoleRepository
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

fun Route.userHasRoleService() {
    val repository = UserHasRoleRepository()
    get(path = "/userHasRole") {
        val usersHasRole = repository.getAll()
        if (usersHasRole.isNotEmpty()) {
            call.respond(message = usersHasRole)
        } else {
            call.respondText(
                text = "No users x role found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/userHasRole/{userId?}/{roleId?}") {
        val userId = call.parameters["userId"]
            ?: return@get call.respondText(
                text = "Missing user id",
                status = HttpStatusCode.BadRequest
            )
        val roleId = call.parameters["roleId"]
            ?: return@get call.respondText(
                text = "Missing role id",
                status = HttpStatusCode.BadRequest
            )
        val userHasRole = repository.findById(
            userId = userId.toInt(),
            roleId = roleId.toInt()
        ) ?: return@get call.respondText(
            text = "No user x role with uId: $userId or rId: $roleId found",
            status = HttpStatusCode.NotFound
        )
        call.respond(message = userHasRole)
    }
    post(path = "/userHasRole") {
        val userHasRole = call.receiveNullable<UserHasRole>()
            ?: return@post call.respondText(
                text = "No user x role body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(userHasRole.toString())
        if (repository.insert(userHasRole = userHasRole)) {
            call.respondText(
                text = "User x role stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "User or role not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/userHasRole/{userId?}/{roleId?}") {
        val userHasRole = call.receiveNullable<UserHasRole>()
            ?: return@put call.respondText(
                text = "No user x role body",
                status = HttpStatusCode.BadRequest
            )
        val userId = call.parameters["userId"]
            ?: return@put call.respondText(
                text = "Missing user id",
                status = HttpStatusCode.BadRequest
            )
        val roleId = call.parameters["roleId"]
            ?: return@put call.respondText(
                text = "Missing role id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(userHasRole.toString())
        if (repository.update(
                userId = userId.toInt(),
                roleId = roleId.toInt(),
                userHasRole = userHasRole
            )
        ) {
            call.respondText(
                text = "User x role update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No user x role stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/userHasRole/{userId?}/{roleId?}") {
        val userId = call.parameters["userId"]
            ?: return@delete call.respondText(
                text = "Missing user id",
                status = HttpStatusCode.BadRequest
            )
        val roleId = call.parameters["roleId"]
            ?: return@delete call.respondText(
                text = "Missing role id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(
                userId = userId.toInt(),
                roleId = roleId.toInt()
            )
        ) {
            call.respondText(
                text = "User x role removed correctly",
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