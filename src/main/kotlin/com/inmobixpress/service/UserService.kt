package com.inmobixpress.service

import com.inmobixpress.model.User
import com.inmobixpress.repository.UserRepository
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

fun Route.userService() {
    val repository = UserRepository()
    get(path = "/user") {
        val users = repository.getAll()
        if (users.isNotEmpty()) {
            call.respond(message = users)
        } else {
            call.respondText(
                text = "No users found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/user/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val user = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No user with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = user)
    }
    post(path = "/user") {
        val user = call.receiveNullable<User>()
            ?: return@post call.respondText(
                text = "No user body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(user.toString())
        if (repository.insert(user = user)) {
            call.respondText(
                text = "User stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Document type or country not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/user/{id?}") {
        val user = call.receiveNullable<User>()
            ?: return@put call.respondText(
                text = "No user body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(user.toString())
        if (repository.update(id = id.toInt(), user = user)) {
            call.respondText(
                text = "User update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No user stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/user/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "User removed correctly",
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