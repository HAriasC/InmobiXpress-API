package com.inmobixpress.service

import com.inmobixpress.model.Department
import com.inmobixpress.repository.DepartmentRepository
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

fun Route.departmentService() {
    val repository = DepartmentRepository()
    get(path = "/department") {
        val departments = repository.getAll()
        if (departments.isNotEmpty()) {
            call.respond(message = departments)
        } else {
            call.respondText(
                text = "No departments found",
                status = HttpStatusCode.OK
            )
        }
    }
    get(path = "/department/{id?}") {
        val id = call.parameters["id"]
            ?: return@get call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        val department = repository.findById(id = id.toInt())
            ?: return@get call.respondText(
                text = "No department with id: $id found",
                status = HttpStatusCode.NotFound
            )
        call.respond(message = department)
    }
    post(path = "/department") {
        val department = call.receiveNullable<Department>()
            ?: return@post call.respondText(
                text = "No department body",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(department.toString())
        if (repository.insert(department = department)){
            call.respondText(
                text = "Department stored correctly",
                status = HttpStatusCode.Created
            )
        } else {
            call.respondText(
                text = "Country not found",
                status = HttpStatusCode.NotFound
            )
        }
    }
    put(path = "/department/{id?}") {
        val department = call.receiveNullable<Department>()
            ?: return@put call.respondText(
                text = "No department body",
                status = HttpStatusCode.BadRequest
            )
        val id = call.parameters["id"]
            ?: return@put call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        call.application.log.debug(department.toString())
        if (repository.update(id = id.toInt(), department = department)) {
            call.respondText(
                text = "Department update correctly",
                status = HttpStatusCode.OK
            )
        } else {
            call.respondText(
                text = "No department stored correctly",
                status = HttpStatusCode.BadRequest
            )
        }
    }
    delete(path = "/department/{id?}") {
        val id = call.parameters["id"]
            ?: return@delete call.respondText(
                text = "Missing id",
                status = HttpStatusCode.BadRequest
            )
        if (repository.delete(id = id.toInt())) {
            call.respondText(
                text = "Department removed correctly",
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