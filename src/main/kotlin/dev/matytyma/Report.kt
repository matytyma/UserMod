package dev.matytyma

import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.matytyma.service.ReportService.pendingReports
import java.time.LocalDateTime

data class Report(
    val message: Message,
    val reportMessage: Message,
    val reporter: User,
) {
    val confirms = mutableListOf<User>()
    val rejects = mutableListOf<User>()
    val timestamp = LocalDateTime.now()
}

val Message.report get() = pendingReports[id]!!
