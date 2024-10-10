package com.inmobixpress.database

import com.inmobixpress.model.Countries
import com.inmobixpress.model.Departments
import com.inmobixpress.model.Devices
import com.inmobixpress.model.Districts
import com.inmobixpress.model.DocumentTypes
import com.inmobixpress.model.Features
import com.inmobixpress.model.FeaturesHasProperty
import com.inmobixpress.model.Historicals
import com.inmobixpress.model.Images
import com.inmobixpress.model.Locations
import com.inmobixpress.model.OfferTypes
import com.inmobixpress.model.Permissions
import com.inmobixpress.model.Properties
import com.inmobixpress.model.PropertiesHasOfferType
import com.inmobixpress.model.PropertyStates
import com.inmobixpress.model.PropertyTypes
import com.inmobixpress.model.Provinces
import com.inmobixpress.model.PublishingStates
import com.inmobixpress.model.Publishings
import com.inmobixpress.model.RequestStates
import com.inmobixpress.model.RequestTypes
import com.inmobixpress.model.Requests
import com.inmobixpress.model.RequestsHasPublishing
import com.inmobixpress.model.Roles
import com.inmobixpress.model.RolesHasPermission
import com.inmobixpress.model.Spaces
import com.inmobixpress.model.Users
import com.inmobixpress.model.UsersHasRole
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val database = Database.connect(
            url = "jdbc:mysql://35.223.62.140:3306/InmobiXpressDB",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "admin",
            password = "123456"
        )
        transaction(database) {
            SchemaUtils.create(Countries)
            SchemaUtils.create(Departments)
            SchemaUtils.create(Devices)
            SchemaUtils.create(Districts)
            SchemaUtils.create(DocumentTypes)
            SchemaUtils.create(Features)
            SchemaUtils.create(FeaturesHasProperty)
            SchemaUtils.create(Historicals)
            SchemaUtils.create(Images)
            SchemaUtils.create(Locations)
            SchemaUtils.create(OfferTypes)
            SchemaUtils.create(Permissions)
            SchemaUtils.create(Properties)
            SchemaUtils.create(PropertiesHasOfferType)
            SchemaUtils.create(PropertyStates)
            SchemaUtils.create(PropertyTypes)
            SchemaUtils.create(Provinces)
            SchemaUtils.create(Publishings)
            SchemaUtils.create(PublishingStates)
            SchemaUtils.create(Requests)
            SchemaUtils.create(RequestsHasPublishing)
            SchemaUtils.create(RequestStates)
            SchemaUtils.create(RequestTypes)
            SchemaUtils.create(Roles)
            SchemaUtils.create(RolesHasPermission)
            SchemaUtils.create(Spaces)
            SchemaUtils.create(Users)
            SchemaUtils.create(UsersHasRole)
        }
    }
}

suspend fun <T> query(block: suspend () -> T): T {
    return newSuspendedTransaction(Dispatchers.IO) { block() }
}