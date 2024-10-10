package com.inmobixpress.service

import com.inmobixpress.model.RequestType
import com.inmobixpress.repository.RequestTypeRepository
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

fun Route.requestTypeService() {
    val repository = RequestTypeRepository()
    get(path = "/requestType") {
        val requestTypes = repository.getAll()
        if (requestTypes.isNotEmpty()) {
            call.respond(message = requestTypes)
        } else {
            call.respondText(
                text = "No request types found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/requestType/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val requestType = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No request type with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = requestType)
    }
    post(path = "/requestType") {
        val requestType = call.receiveNullable<RequestType>()
            ?: return@post call.respondText(
                text = "No request type body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(requestType.toString())
        repository.insert(requestType = requestType)
        call.respondText(
            text = "Request type stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/requestType/{id?}") {
        val requestType = call.receiveNullable<RequestType>()
            ?: return@put call.respondText(
                text = "No request type body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(requestType.toString())
        if (repository.update(id = id.toInt(), requestType = requestType)) {
            call.respondText(
                text = "Request type update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No request type stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/requestType/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Request type removed correctly",
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