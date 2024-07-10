package dev.matytyma.service

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.matytyma.Report

object ReportService {
    val pendingReportedMessages = mutableMapOf<User, Message>()
    val pendingReports = mutableMapOf<Snowflake, Report>()
}
