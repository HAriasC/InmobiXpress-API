package com.inmobixpress.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@Serializable
data class Request(
    val id: Int,
    val date: LocalDateTime,
    val message: String,
    val requestType: RequestType,
    val requestState: RequestState,
    val user: User
)

object Requests: IntIdTable(name = "Request") {
    val date = datetime(name = "date")
    val message = varchar(name = "message", length = 2000)
    val requestType = reference(name = "RequestType_id", foreign = RequestTypes)
    val requestState = reference(name = "RequestState_id", foreign = RequestStates)
    val user = reference(name = "User_id", foreign = Users)
}