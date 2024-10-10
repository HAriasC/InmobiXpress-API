package com.inmobixpress.model

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

data class SendMessage(
    val notification: NotificationBody,
    val token: String = "",
    val topic: String = ""
)

data class NotificationBody(
    val title: String,
    val body: String
)

fun SendMessage.toMessage(): Message {
    return Message.builder()
        .setNotification(
            Notification.builder()
                .setTitle(notification.title)
                .setBody(notification.body)
                .build()
        ).apply {
            if (token.isEmpty()) {
                setTopic(topic)
            } else {
                setToken(token)
            }
        }.build()
}
