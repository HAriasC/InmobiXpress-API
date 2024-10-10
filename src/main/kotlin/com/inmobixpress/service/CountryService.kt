package com.inmobixpress.service

import com.inmobixpress.model.Country
import com.inmobixpress.repository.CountryRepository
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

fun Route.countryService() {
    val repository = CountryRepository()
    get(path = "/country") {
        val countries = repository.getAll()
        if (countries.isNotEmpty()) {
            call.respond(message = countries)
        } else {
            call.respondText(
                text = "No countries found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/country/{id?}") {
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
    post(path = "/country") {
        val country = call.receiveNullable<Country>()
            ?: return@post call.respondText(
                text = "No country body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(country.toString())
        repository.insert(country = country)
        call.respondText(
            text = "Country stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/country/{id?}") {
        val country = call.receiveNullable<Country>()
            ?: return@put call.respondText(
                text = "No country body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(country.toString())
        if (repository.update(id = id.toInt(), country = country)) {
            call.respondText(
                text = "Country update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No country stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/country/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Country removed correctly",
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