package com.inmobixpress.service

import com.inmobixpress.model.RequestState
import com.inmobixpress.repository.RequestStateRepository
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

fun Route.requestStateService() {
    val repository = RequestStateRepository()
    get(path = "/requestState") {
        val requestStates = repository.getAll()
        if (requestStates.isNotEmpty()) {
            call.respond(message = requestStates)
        } else {
            call.respondText(
                text = "No request states found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/requestState/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val requestState = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No request state with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = requestState)
    }
    post(path = "/requestState") {
        val requestState = call.receiveNullable<RequestState>()
            ?: return@post call.respondText(
                text = "No request state body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(requestState.toString())
        repository.insert(requestState = requestState)
        call.respondText(
            text = "Request state stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/requestState/{id?}") {
        val requestState = call.receiveNullable<RequestState>()
            ?: return@put call.respondText(
                text = "No request state body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(requestState.toString())
        if (repository.update(id = id.toInt(), requestState = requestState)) {
            call.respondText(
                text = "Request state update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No request state stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/requestState/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Request state removed correctly",
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