package com.inmobixpress.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application

fun Application.configureFirebase() {
    val serviceAccount = this::class.java.classLoader.getResourceAsStream(
        "service_account_key.json"
    )
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
}