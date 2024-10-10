package com.inmobixpress.service

import com.inmobixpress.model.OfferType
import com.inmobixpress.repository.OfferTypeRepository
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

fun Route.offerTypeService() {
    val repository = OfferTypeRepository()
    get(path = "/offerType") {
        val offerTypes = repository.getAll()
        if (offerTypes.isNotEmpty()) {
            call.respond(message = offerTypes)
        } else {
            call.respondText(
                text = "No offerType found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/offerType/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val offerType = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No offerType with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = offerType)
    }
    post(path = "/offerType") {
        val offerType = call.receiveNullable<OfferType>()
            ?: return@post call.respondText(
                text = "No offer type body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(offerType.toString())
        repository.insert(offerType = offerType)
        call.respondText(
            text = "OfferType stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/offerType/{id?}") {
        val offerType = call.receiveNullable<OfferType>()
            ?: return@put call.respondText(
                text = "No offer type body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(offerType.toString())
        if (repository.update(id = id.toInt(), offerType = offerType)) {
            call.respondText(
                text = "OfferType update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No offerType stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/offerType/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "OfferType removed correctly",
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