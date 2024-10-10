package com.inmobixpress.service

import com.inmobixpress.model.Space
import com.inmobixpress.repository.SpaceRepository
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

fun Route.spaceService() {
    val repository = SpaceRepository()
    get(path = "/space") {
        val spaces = repository.getAll()
        if (spaces.isNotEmpty()) {
            call.respond(message = spaces)
        } else {
            call.respondText(
                text = "No spaces found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/space/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val space = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No space with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = space)
    }
    post(path = "/space") {
        val space = call.receiveNullable<Space>()
            ?: return@post call.respondText(
                text = "No space body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(space.toString())
        repository.insert(space = space)
        call.respondText(
            text = "Space stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/space/{id?}") {
        val space = call.receiveNullable<Space>()
            ?: return@put call.respondText(
                text = "No space body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(space.toString())
        if (repository.update(id = id.toInt(), space = space)) {
            call.respondText(
                text = "Space update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No space stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/space/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Space removed correctly",
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