package com.inmobixpress.service

import com.inmobixpress.model.PublishingState
import com.inmobixpress.repository.PublishingStateRepository
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

fun Route.publishingStateService() {
    val repository = PublishingStateRepository()
    get(path = "/publishingState") {
        val publishingStates = repository.getAll()
        if (publishingStates.isNotEmpty()) {
            call.respond(message = publishingStates)
        } else {
            call.respondText(
                text = "No publishing states found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/publishingState/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val publishingState = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No publishing state with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = publishingState)
    }
    post(path = "/publishingState") {
        val publishingState = call.receiveNullable<PublishingState>()
            ?: return@post call.respondText(
                text = "No publishing state body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(publishingState.toString())
        repository.insert(publishingState = publishingState)
        call.respondText(
            text = "Publishing state stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/publishingState/{id?}") {
        val publishingState = call.receiveNullable<PublishingState>()
            ?: return@put call.respondText(
                text = "No publishing state body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(publishingState.toString())
        if (repository.update(id = id.toInt(), publishingState = publishingState)) {
            call.respondText(
                text = "Publishing state update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No publishing state stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/publishingState/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Publishing state removed correctly",
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