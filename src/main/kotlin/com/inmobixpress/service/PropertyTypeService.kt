package com.inmobixpress.service

import com.inmobixpress.model.PropertyType
import com.inmobixpress.repository.PropertyTypeRepository
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

fun Route.propertyTypeService() {
    val repository = PropertyTypeRepository()
    get(path = "/propertyType") {
        val propertyTypes = repository.getAll()
        if (propertyTypes.isNotEmpty()) {
            call.respond(message = propertyTypes)
        } else {
            call.respondText(
                text = "No property types found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/propertyType/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val propertyType = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No property type with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = propertyType)
    }
    post(path = "/propertyType") {
        val propertyType = call.receiveNullable<PropertyType>()
            ?: return@post call.respondText(
                text = "No property type body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(propertyType.toString())
        repository.insert(propertyType = propertyType)
        call.respondText(
            text = "Property type stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/propertyType/{id?}") {
        val propertyType = call.receiveNullable<PropertyType>()
            ?: return@put call.respondText(
                text = "No property type body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(propertyType.toString())
        if (repository.update(id = id.toInt(), propertyType = propertyType)) {
            call.respondText(
                text = "Property type update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No property type stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/propertyType/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Property type removed correctly",
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