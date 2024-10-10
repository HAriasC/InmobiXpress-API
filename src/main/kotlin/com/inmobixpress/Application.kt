package com.inmobixpress

import com.inmobixpress.database.DatabaseFactory
import com.inmobixpress.plugins.configureFirebase
import com.inmobixpress.plugins.configureRouting
import com.inmobixpress.plugins.configureSerialization
import io.ktor.server.application.Application


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureRouting()
    configureFirebase()
}
