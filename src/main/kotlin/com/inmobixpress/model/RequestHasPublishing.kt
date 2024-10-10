package com.inmobixpress.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@Serializable
data class RequestHasPublishing(
    val request: Request,
    val publishing: Publishing,
    val createDate: LocalDateTime
)

object RequestsHasPublishing : CompositeIdTable(name = "RequestHasPublishing") {
    val request = reference(name = "Request_id", foreign = Requests)
    val publishing = reference(
        name = "Publishing_id",
        foreign = Publishings,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val createDate = datetime(name = "createDate")

    override val primaryKey = PrimaryKey(request, publishing)
}
