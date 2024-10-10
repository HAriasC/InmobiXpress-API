package com.inmobixpress.service

import com.inmobixpress.model.Feature
import com.inmobixpress.repository.FeatureRepository
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

fun Route.featureService() {
    val repository = FeatureRepository()
    get(path = "/feature") {
        val features = repository.getAll()
        if (features.isNotEmpty()) {
            call.respond(message = features)
        } else {
            call.respondText(
                text = "No countries found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/feature/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val feature = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No feature with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = feature)
    }
    post(path = "/feature") {
        val feature = call.receiveNullable<Feature>()
            ?: return@post call.respondText(
                text = "No feature body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(feature.toString())
        repository.insert(feature = feature)
        call.respondText(
            text = "Feature stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/feature/{id?}") {
        val feature = call.receiveNullable<Feature>()
            ?: return@put call.respondText(
                text = "No feature body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(feature.toString())
        if (repository.update(id = id.toInt(), feature = feature)) {
            call.respondText(
                text = "Feature update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No feature stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/feature/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Feature removed correctly",
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