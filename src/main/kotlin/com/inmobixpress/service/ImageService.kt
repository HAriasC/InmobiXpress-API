package com.inmobixpress.service

import com.inmobixpress.model.Image
import com.inmobixpress.repository.ImageRepository
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

fun Route.imageService() {
    val repository = ImageRepository()
    get(path = "/image") {
        val images = repository.getAll()
        if (images.isNotEmpty()) {
            call.respond(message = images)
        } else {
            call.respondText(
                text = "No images found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/image/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val image = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No image with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = image)
    }
    post(path = "/image") {
        val image = call.receiveNullable<Image>()
            ?: return@post call.respondText(
                text = "No image body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(image.toString())
        if (repository.insert(image = image)){
            call.respondText(
                text = "Image stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Property not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/image/{id?}") {
        val image = call.receiveNullable<Image>()
            ?: return@put call.respondText(
                text = "No image body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(image.toString())
        if (repository.update(id = id.toInt(), image = image)) {
            call.respondText(
                text = "Image update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No image stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/image/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Image removed correctly",
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