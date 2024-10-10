package com.inmobixpress.service

import com.inmobixpress.model.Location
import com.inmobixpress.repository.LocationRepository
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

fun Route.locationService() {
    val repository = LocationRepository()
    get(path = "/location") {
        val locations = repository.getAll()
        if (locations.isNotEmpty()) {
            call.respond(message = locations)
        } else {
            call.respondText(
                text = "No locations found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/location/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val country = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No country with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = country)
    }
    post(path = "/location") {
        val location = call.receiveNullable<Location>()
            ?: return@post call.respondText(
                text = "No location body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(location.toString())
        repository.insert(location = location)
        call.respondText(
            text = "Location stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/location/{id?}") {
        val location = call.receiveNullable<Location>()
            ?: return@put call.respondText(
                text = "No location body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(location.toString())
        if (repository.update(id = id.toInt(), location = location)) {
            call.respondText(
                text = "Location update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No location stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/location/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Location removed correctly",
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