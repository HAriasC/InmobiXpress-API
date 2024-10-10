package com.inmobixpress.service

import com.inmobixpress.model.Historical
import com.inmobixpress.repository.HistoricalRepository
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

fun Route.historicalService() {
    val repository = HistoricalRepository()
    get(path = "/historical") {
        val historical = repository.getAll()
        if (historical.isNotEmpty()) {
            call.respond(message = historical)
        } else {
            call.respondText(
                text = "No historical found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/historical/{publishingStateId?}/{publishingId?}") {
        val publishingStateId = call.parameters["publishingStateId"]
            ?: return@get call.respondText(
                text = "Missing publishing state id",
                status = HttpStatusCode.BadRequest
            )
        val publishingId = call.parameters["publishingId"]
            ?: return@get call.respondText(
                text = "Missing publishing id",
                status = HttpStatusCode.BadRequest
            )
        val historical = repository.findById(
            publishingStateId = publishingStateId.toInt(),
            publishingId = publishingId.toInt()
        ) ?: return@get call.respondText(
            text = "No historical with psId: $publishingStateId or pId: $publishingId found",
            status = HttpStatusCode.NotFound
        )
        call.respond(message = historical)
    }
    post(path = "/historical") {
        val historical = call.receiveNullable<Historical>()
            ?: return@post call.respondText(
                text = "No historical body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(historical.toString())
        if (repository.insert(historical = historical)) {
            call.respondText(
                text = "Historical stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Publishing state or publishing not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/historical/{publishingStateId?}/{publishingId?}") {
        val historical = call.receiveNullable<Historical>()
            ?: return@put call.respondText(
                text = "No historical body",
                status = HttpStatusCode.BadRequest
            )
        val publishingStateId = call.parameters["publishingStateId"]
            ?: return@put call.respondText(
                text = "Missing publishing state id",
                status = HttpStatusCode.BadRequest
            )
        val publishingId = call.parameters["publishingId"]
            ?: return@put call.respondText(
                text = "Missing publishing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(historical.toString())
        if (repository.update(
                publishingStateId = publishingStateId.toInt(),
                publishingId = publishingId.toInt(),
                historical = historical
            )
        ) {
            call.respondText(
                text = "Historical update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No historical stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/historical/{publishingStateId?}/{publishingId?}") {
        val publishingStateId = call.parameters["publishingStateId"]
            ?: return@delete call.respondText(
                text = "Missing publishing state id",
                status = HttpStatusCode.BadRequest
            )
        val publishingId = call.parameters["publishingId"]
            ?: return@delete call.respondText(
                text = "Missing publishing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(
                publishingStateId = publishingStateId.toInt(),
                publishingId = publishingId.toInt()
            )
        ) {
            call.respondText(
                text = "Historical removed correctly",
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