package com.inmobixpress.service

import com.inmobixpress.model.Province
import com.inmobixpress.repository.ProvinceRepository
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

fun Route.provinceService() {
    val repository = ProvinceRepository()
    get(path = "/province") {
        val provinces = repository.getAll()
        if (provinces.isNotEmpty()) {
            call.respond(message = provinces)
        } else {
            call.respondText(
                text = "No provinces found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/province/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val province = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No province with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = province)
    }
    post(path = "/province") {
        val province = call.receiveNullable<Province>()
            ?: return@post call.respondText(
                text = "No province body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(province.toString())
        if (repository.insert(province = province)) {
            call.respondText(
                text = "Province stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Department not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/province/{id?}") {
        val province = call.receiveNullable<Province>()
            ?: return@put call.respondText(
                text = "No province body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(province.toString())
        if (repository.update(id = id.toInt(), province = province)) {
            call.respondText(
                text = "Province update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No province stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/province/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Province removed correctly",
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