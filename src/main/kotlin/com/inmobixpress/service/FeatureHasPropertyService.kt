package com.inmobixpress.service

import com.inmobixpress.model.FeatureHasProperty
import com.inmobixpress.repository.FeatureHasPropertyRepository
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

fun Route.featureHasPropertyService() {
    val repository = FeatureHasPropertyRepository()
    get(path = "/featureHasProperty") {
        val featuresHasProperty = repository.getAll()
        if (featuresHasProperty.isNotEmpty()) {
            call.respond(message = featuresHasProperty)
        } else {
            call.respondText(
                text = "No feature x property found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/featureHasProperty/{featureId?}/{propertyId?}") {
        val featureId = call.parameters["featureId"]
            ?: return@get call.respondText(
                text = "Missing feature id",
                status = HttpStatusCode.BadRequest
            )
        val propertyId = call.parameters["propertyId"]
            ?: return@get call.respondText(
                text = "Missing property id",
                status = HttpStatusCode.BadRequest
            )
        val featureHasProperty = repository.findById(
            featureId = featureId.toInt(),
            propertyId = propertyId.toInt()
        ) ?: return@get call.respondText(
            text = "No feature x property with fId: $featureId or pId: $propertyId found",
            status = HttpStatusCode.NotFound
        )
        call.respond(message = featureHasProperty)
    }
    post(path = "/featureHasProperty") {
        val featureHasProperty = call.receiveNullable<FeatureHasProperty>()
            ?: return@post call.respondText(
                text = "No feature x property body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(featureHasProperty.toString())
        if (repository.insert(featureHasProperty = featureHasProperty)) {
            call.respondText(
                text = "Feature x property stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Feature or property not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/featureHasProperty/{featureId?}/{propertyId?}") {
        val featureHasProperty = call.receiveNullable<FeatureHasProperty>()
            ?: return@put call.respondText(
                text = "No feature x property body",
                status = HttpStatusCode.BadRequest
            )
        val featureId = call.parameters["featureId"]
            ?: return@put call.respondText(
                text = "Missing feature id",
                status = HttpStatusCode.BadRequest
            )
        val propertyId = call.parameters["propertyId"]
            ?: return@put call.respondText(
                text = "Missing property id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(featureHasProperty.toString())
        if (repository.update(
                featureId = featureId.toInt(),
                propertyId = propertyId.toInt(),
                featureHasProperty = featureHasProperty
            )
        ) {
            call.respondText(
                text = "Feature x property update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No feature x property stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/featureHasProperty/{featureId?}/{propertyId?}") {
        val featureId = call.parameters["featureId"]
            ?: return@delete call.respondText(
                text = "Missing feature id",
                status = HttpStatusCode.BadRequest
            )
        val propertyId = call.parameters["propertyId"]
            ?: return@delete call.respondText(
                text = "Missing property id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(
                featureId = featureId.toInt(),
                propertyId = propertyId.toInt()
            )
        ) {
            call.respondText(
                text = "Feature x property removed correctly",
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