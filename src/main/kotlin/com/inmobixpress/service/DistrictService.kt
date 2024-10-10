package com.inmobixpress.service

import com.inmobixpress.model.District
import com.inmobixpress.repository.DistrictRepository
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

fun Route.districtService() {
    val repository = DistrictRepository()
    get(path = "/district") {
        val districts = repository.getAll()
        if (districts.isNotEmpty()) {
            call.respond(message = districts)
        } else {
            call.respondText(
                text = "No districts found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/district/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val district = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No district with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = district)
    }
    post(path = "/district") {
        val district = call.receiveNullable<District>()
            ?: return@post call.respondText(
                text = "No district body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(district.toString())
        if (repository.insert(district = district)){
            call.respondText(
                text = "District stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Province or location not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/district/{id?}") {
        val district = call.receiveNullable<District>()
            ?: return@put call.respondText(
                text = "No district body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(district.toString())
        if (repository.update(id = id.toInt(), district = district)) {
            call.respondText(
                text = "District update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No district stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/district/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "District removed correctly",
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