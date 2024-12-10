package com.inmobixpress.service

import com.inmobixpress.model.Property
import com.inmobixpress.repository.LocationRepository
import com.inmobixpress.repository.PropertyRepository
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
import kotlinx.coroutines.delay

fun Route.propertyService() {
    val repository = PropertyRepository()
    val locationRepository = LocationRepository()
    get(path = "/property") {
        val properties = repository.getAll()
        if (properties.isNotEmpty()) {
            call.respond(message = properties)
        } else {
            call.respondText(
                text = "No properties found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/property/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val property = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No property with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = property)
    }
    post(path = "/property") {
        val property = call.receiveNullable<Property>()
            ?: return@post call.respondText(
                text = "No property body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(property.toString())
        locationRepository.insert(location = property.location)
        val location = locationRepository.getAll().last()
        property.location = location
        delay(500)
        if (repository.insert(property = property)) {
            val last = repository.getAll().last()
            call.respondText(
                text = "Property stored correctly|id:${last.id}",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Property type, property state, location, district or user not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/property/{id?}") {
        val property = call.receiveNullable<Property>()
            ?: return@put call.respondText(
                text = "No property body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(property.toString())
        if (repository.update(id = id.toInt(), property = property)) {
            call.respondText(
                text = "Property update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No property stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/property/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Property removed correctly",
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