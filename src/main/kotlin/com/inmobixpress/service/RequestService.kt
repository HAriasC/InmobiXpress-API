package com.inmobixpress.service

import com.inmobixpress.model.Request
import com.inmobixpress.repository.RequestRepository
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

fun Route.requestService() {
    val repository = RequestRepository()
    get(path = "/request") {
        val requests = repository.getAll()
        if (requests.isNotEmpty()) {
            call.respond(message = requests)
        } else {
            call.respondText(
                text = "No requests found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/request/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val request = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No request with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = request)
    }
    post(path = "/request") {
        val request = call.receiveNullable<Request>()
            ?: return@post call.respondText(
                text = "No request body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(request.toString())
        if (repository.insert(request = request)){
            call.respondText(
                text = "Request stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Request type, request state or user not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/request/{id?}") {
        val request = call.receiveNullable<Request>()
            ?: return@put call.respondText(
                text = "No request body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(request.toString())
        if (repository.update(id = id.toInt(), request = request)) {
            call.respondText(
                text = "Request update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No request stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/request/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Request removed correctly",
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