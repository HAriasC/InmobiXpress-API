package com.inmobixpress.service

import com.inmobixpress.repository.LoginRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.loginService() {
    val repository = LoginRepository()
    post(path = "/login") {
        val username = call.queryParameters["username"]
            ?: return@post call.respondText(
                text = "Missing username",
                status = HttpStatusCode.BadRequest
            )
        val password = call.queryParameters["password"]
            ?: return@post call.respondText(
                text = "Missing password",
                status = HttpStatusCode.BadRequest
            )
        val user = repository.login(username = username, password = password)
            ?: return@post call.respondText(
                text = "Authorization Failed! Try Logging In again.",
                status = HttpStatusCode.Unauthorized
            )
        call.respond(message = user)
    }
}