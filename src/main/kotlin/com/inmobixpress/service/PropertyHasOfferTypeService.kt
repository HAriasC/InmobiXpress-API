package com.inmobixpress.service

import com.inmobixpress.model.PropertyHasOfferType
import com.inmobixpress.repository.PropertyHasOfferTypeRepository
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

fun Route.propertyHasOfferTypeService() {
    val repository = PropertyHasOfferTypeRepository()
    get(path = "/propertyHasOfferType") {
        val propertiesHasOfferType = repository.getAll()
        if (propertiesHasOfferType.isNotEmpty()) {
            call.respond(message = propertiesHasOfferType)
        } else {
            call.respondText(
                text = "No properties x offer type found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/propertyHasOfferType/{propertyId?}/{offerTypeId?}") {
        val propertyId = call.parameters["propertyId"]
            ?: return@get call.respondText(
                text = "Missing property id",
                status = HttpStatusCode.BadRequest
            )
        val offerTypeId = call.parameters["publishingId"]
            ?: return@get call.respondText(
                text = "Missing offer type id",
                status = HttpStatusCode.BadRequest
            )
        val propertyHasOfferType = repository.findById(
            propertyId = propertyId.toInt(),
            offerTypeId = offerTypeId.toInt()
        ) ?: return@get call.respondText(
            text = "No property x offer type with pId: $propertyId or otId: $offerTypeId found",
            status = HttpStatusCode.NotFound
        )
        call.respond(message = propertyHasOfferType)
    }
    post(path = "/propertyHasOfferType") {
        val propertyHasOfferType = call.receiveNullable<PropertyHasOfferType>()
            ?: return@post call.respondText(
                text = "No property x offer type body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(propertyHasOfferType.toString())
        if (repository.insert(propertyHasOfferType = propertyHasOfferType)) {
            call.respondText(
                text = "Property x offer type stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Property or offer type not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/propertyHasOfferType/{propertyId?}/{offerTypeId?}") {
        val propertyHasOfferType = call.receiveNullable<PropertyHasOfferType>()
            ?: return@put call.respondText(
                text = "No property x offer type body",
                status = HttpStatusCode.BadRequest
            )
        val propertyId = call.parameters["propertyId"]
            ?: return@put call.respondText(
                text = "Missing property id",
                status = HttpStatusCode.BadRequest
            )
        val offerTypeId = call.parameters["offerTypeId"]
            ?: return@put call.respondText(
                text = "Missing offer type id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(propertyHasOfferType.toString())
        if (repository.update(
                propertyId = propertyId.toInt(),
                offerTypeId = offerTypeId.toInt(),
                propertyHasOfferType = propertyHasOfferType
            )
        ) {
            call.respondText(
                text = "Property x offer type update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No property x offer type stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/propertyHasOfferType/{propertyId?}/{offerTypeId?}") {
        val propertyId = call.parameters["propertyId"]
            ?: return@delete call.respondText(
                text = "Missing publishing state id",
                status = HttpStatusCode.BadRequest
            )
        val offerTypeId = call.parameters["offerTypeId"]
            ?: return@delete call.respondText(
                text = "Missing offer type id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(
                propertyId = propertyId.toInt(),
                offerTypeId = offerTypeId.toInt()
            )
        ) {
            call.respondText(
                text = "Property x offer type removed correctly",
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