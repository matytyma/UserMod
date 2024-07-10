package dev.matytyma.service

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Message
import dev.kord.core.entity.User
import dev.matytyma.Report

object ReportService {
    const val REPORT_SCORE_THRESHOLD = 5

    val unfinishedReports = mutableMapOf<User, Message>()
    val pendingReports = mutableMapOf<Snowflake, Report>()
}
