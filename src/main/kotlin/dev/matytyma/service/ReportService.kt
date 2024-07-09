package dev.matytyma.service

import dev.kord.core.entity.Message
import dev.kord.core.entity.User

object ReportService {
    val pendingReportedMessages = mutableMapOf<User, Message>()
}
