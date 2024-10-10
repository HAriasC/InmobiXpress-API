package com.inmobixpress.service

import com.inmobixpress.model.PropertyState
import com.inmobixpress.repository.PropertyStateRepository
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

fun Route.propertyStateService() {
    val repository = PropertyStateRepository()
    get(path = "/propertyState") {
        val propertyStates = repository.getAll()
        if (propertyStates.isNotEmpty()) {
            call.respond(message = propertyStates)
        } else {
            call.respondText(
                text = "No property states found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/propertyState/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val propertyState = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No property state with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = propertyState)
    }
    post(path = "/propertyState") {
        val propertyState = call.receiveNullable<PropertyState>()
            ?: return@post call.respondText(
                text = "No property state body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(propertyState.toString())
        repository.insert(propertyState = propertyState)
        call.respondText(
            text = "Property state stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/propertyState/{id?}") {
        val propertyState = call.receiveNullable<PropertyState>()
            ?: return@put call.respondText(
                text = "No property state body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(propertyState.toString())
        if (repository.update(id = id.toInt(), propertyState = propertyState)) {
            call.respondText(
                text = "Property state update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No property state stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/propertyState/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Property state removed correctly",
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