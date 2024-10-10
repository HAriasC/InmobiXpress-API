package com.inmobixpress.service

import com.google.firebase.messaging.FirebaseMessaging
import com.inmobixpress.model.SendMessage
import com.inmobixpress.model.toMessage
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.log
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.notificationService() {
    post(path = "/sendNotification") {
        val notification = call.receiveNullable<SendMessage>()
            ?: return@post call.respondText(
                text = "No notification body",
                status = HttpStatusCode.BadRequest
            )
        val response = FirebaseMessaging.getInstance().send(notification.toMessage())
        call.application.log.debug(notification.toString() + response)
        call.respondText(
            text = "Notification sent successfully",
            status = HttpStatusCode.OK
        )
    }
}