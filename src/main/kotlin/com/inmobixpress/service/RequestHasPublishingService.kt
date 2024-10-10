package com.inmobixpress.service

import com.inmobixpress.model.RequestHasPublishing
import com.inmobixpress.repository.RequestHasPublishingRepository
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

fun Route.requestHasPublishingService() {
    val repository = RequestHasPublishingRepository()
    get(path = "/requestHasPublishing") {
        val requestsHasPublishing = repository.getAll()
        if (requestsHasPublishing.isNotEmpty()) {
            call.respond(message = requestsHasPublishing)
        } else {
            call.respondText(
                text = "No requests x publishing found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/requestHasPublishing/{requestId?}/{publishingId?}") {
        val requestId = call.parameters["requestId"]
            ?: return@get call.respondText(
                text = "Missing request id",
                status = HttpStatusCode.BadRequest
            )
        val publishingId = call.parameters["publishingId"]
            ?: return@get call.respondText(
                text = "Missing publishing id",
                status = HttpStatusCode.BadRequest
            )
        val requestHasPublishing = repository.findById(
            requestId = requestId.toInt(),
            publishingId = publishingId.toInt()
        ) ?: return@get call.respondText(
            text = "No request x publishing with rId: $requestId or pId: $publishingId found",
            status = HttpStatusCode.NotFound
        )
        call.respond(message = requestHasPublishing)
    }
    post(path = "/requestHasPublishing") {
        val requestHasPublishing = call.receiveNullable<RequestHasPublishing>()
            ?: return@post call.respondText(
                text = "No request x publishing body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(requestHasPublishing.toString())
        if (repository.insert(requestHasPublishing = requestHasPublishing)) {
            call.respondText(
                text = "Request x publishing stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Request or publishing not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/requestHasPublishing/{requestId?}/{publishingId?}") {
        val requestHasPublishing = call.receiveNullable<RequestHasPublishing>()
            ?: return@put call.respondText(
                text = "No request x publishing body",
                status = HttpStatusCode.BadRequest
            )
        val requestId = call.parameters["requestId"]
            ?: return@put call.respondText(
                text = "Missing request id",
                status = HttpStatusCode.BadRequest
            )
        val publishingId = call.parameters["publishingId"]
            ?: return@put call.respondText(
                text = "Missing publishing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(requestHasPublishing.toString())
        if (repository.update(
                requestId = requestId.toInt(),
                publishingId = publishingId.toInt(),
                requestHasPublishing = requestHasPublishing
            )
        ) {
            call.respondText(
                text = "Request x publishing update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No request x publishing stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/requestHasPublishing/{requestId?}/{publishingId?}") {
        val requestId = call.parameters["requestId"]
            ?: return@delete call.respondText(
                text = "Missing request id",
                status = HttpStatusCode.BadRequest
            )
        val publishingId = call.parameters["publishingId"]
            ?: return@delete call.respondText(
                text = "Missing publishing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(
                requestId = requestId.toInt(),
                publishingId = publishingId.toInt()
            )
        ) {
            call.respondText(
                text = "Request x publishing removed correctly",
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