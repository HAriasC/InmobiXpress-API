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
    val date: LocalDateTime
)

object Historicals : CompositeIdTable(name = "Historical") {
    val publishingState = reference(name = "PublishingState_id", foreign = PublishingStates)
    val publishing = reference(
        name = "Publishing_id",
        foreign = Publishings,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
    val date = datetime(name = "date")

    override val primaryKey = PrimaryKey(publishingState, publishing)
}
