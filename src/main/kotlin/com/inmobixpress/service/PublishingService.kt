package com.inmobixpress.service

import com.inmobixpress.model.Publishing
import com.inmobixpress.repository.PublishingRepository
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

fun Route.publishingService() {
    val repository = PublishingRepository()
    get(path = "/publishing") {
        val publishing = repository.getAll()
        if (publishing.isNotEmpty()) {
            call.respond(message = publishing)
        } else {
            call.respondText(
                text = "No publishing found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/publishing/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val publishing = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No publishing with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = publishing)
    }
    post(path = "/publishing") {
        val publishing = call.receiveNullable<Publishing>()
            ?: return@post call.respondText(
                text = "No publishing body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(publishing.toString())
        if (repository.insert(publishing = publishing)) {
            call.respondText(
                text = "Publishing stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Property not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/publishing/{id?}") {
        val publishing = call.receiveNullable<Publishing>()
            ?: return@put call.respondText(
                text = "No publishing body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(publishing.toString())
        if (repository.update(id = id.toInt(), publishing = publishing)) {
            call.respondText(
                text = "Publishing update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No publishing stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/publishing/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Publishing removed correctly",
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