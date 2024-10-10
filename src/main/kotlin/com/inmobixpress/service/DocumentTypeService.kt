package com.inmobixpress.service

import com.inmobixpress.model.DocumentType
import com.inmobixpress.repository.DocumentTypeRepository
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

fun Route.documentTypeService() {
    val repository = DocumentTypeRepository()
    get(path = "/documentType") {
        val documentType = repository.getAll()
        if (documentType.isNotEmpty()) {
            call.respond(message = documentType)
        } else {
            call.respondText(
                text = "No document types found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/documentType/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val documentType = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No document type with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = documentType)
    }
    post(path = "/documentType") {
        val documentType = call.receiveNullable<DocumentType>()
            ?: return@post call.respondText(
                text = "No document type body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(documentType.toString())
        repository.insert(documentType = documentType)
        call.respondText(
            text = "Document type stored correctly",
            status = HttpStatusCode.Created
        )
    }
    put(path = "/documentType/{id?}") {
        val documentType = call.receiveNullable<DocumentType>()
            ?: return@put call.respondText(
                text = "No document type body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(documentType.toString())
        if (repository.update(id = id.toInt(), documentType = documentType)) {
            call.respondText(
                text = "Document type update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No document type stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/documentType/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Document type removed correctly",
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