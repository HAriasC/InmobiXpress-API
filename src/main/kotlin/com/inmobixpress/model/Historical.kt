package com.inmobixpress.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.CompositeIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

@Serializable
data class Historical(
    val publishingState: PublishingState,
    val publishing: Publishing,
    val startDate: LocalDateTime,
    val finishDate: LocalDateTime,
    val contract: String
)

object Historicals : CompositeIdTable(name = "Historical") {
    val publishingState = reference(name = "PublishingState_id", foreign = PublishingStates)
    val publishing = reference(
        name = "Publishing_id",
        foreign = Publishings,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val startDate = datetime(name = "startDate")
    val finishDate = datetime(name = "finishDate")
    val contract = varchar(name = "contract", length = 2000)

    override val primaryKey = PrimaryKey(publishingState, publishing)
}
