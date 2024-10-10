package com.inmobixpress.plugins

import com.inmobixpress.service.districtService
import com.inmobixpress.service.countryService
import com.inmobixpress.service.departmentService
import com.inmobixpress.service.deviceService
import com.inmobixpress.service.documentTypeService
import com.inmobixpress.service.featureHasPropertyService
import com.inmobixpress.service.featureService
import com.inmobixpress.service.historicalService
import com.inmobixpress.service.imageService
import com.inmobixpress.service.locationService
import com.inmobixpress.service.notificationService
import com.inmobixpress.service.offerTypeService
import com.inmobixpress.service.permissionService
import com.inmobixpress.service.propertyHasOfferTypeService
import com.inmobixpress.service.propertyService
import com.inmobixpress.service.propertyStateService
import com.inmobixpress.service.propertyTypeService
import com.inmobixpress.service.provinceService
import com.inmobixpress.service.publishingService
import com.inmobixpress.service.publishingStateService
import com.inmobixpress.service.requestHasPublishingService
import com.inmobixpress.service.requestService
import com.inmobixpress.service.requestStateService
import com.inmobixpress.service.requestTypeService
import com.inmobixpress.service.roleHasPermissionService
import com.inmobixpress.service.roleService
import com.inmobixpress.service.spaceService
import com.inmobixpress.service.userHasRoleService
import com.inmobixpress.service.userService
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        welcomePage()
        countryService()
        departmentService()
        deviceService()
        districtService()
        documentTypeService()
        featureService()
        featureHasPropertyService()
        historicalService()
        imageService()
        locationService()
        notificationService()
        offerTypeService()
        permissionService()
        propertyService()
        propertyHasOfferTypeService()
        propertyStateService()
        propertyTypeService()
        provinceService()
        publishingService()
        publishingStateService()
        requestService()
        requestHasPublishingService()
        requestStateService()
        requestTypeService()
        roleService()
        roleHasPermissionService()
        spaceService()
        userService()
        userHasRoleService()
    }
}

fun Route.welcomePage() {
    get(path = "/") {
        call.respondText(
            text = "InmobiXpress Service"
        )
        staticResources("/static", "static")
    }
}
