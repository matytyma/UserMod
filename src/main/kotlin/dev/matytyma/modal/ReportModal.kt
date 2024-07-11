package dev.matytyma.modal

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.effectiveName
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed
import dev.matytyma.*
import dev.matytyma.service.ReportService.REPORT_SCORE_THRESHOLD
import dev.matytyma.service.ReportService.pendingReports
import dev.matytyma.service.ReportService.queuedReports
import dev.matytyma.service.ReportService.unfinishedReports

object ReportModal : ModalExecutor {
    private val REPORT_ROLE_MENTION = "<@&${dotenv["REPORT_ROLE_ID"]}>"

    override suspend fun onSubmit(interaction: ModalSubmitInteraction) {
        val message = unfinishedReports.remove(interaction.user) ?: return
        val author = message.author ?: return
        val user = interaction.user


        if (synchronized(message) { queuedReports.contains(message) }) {
            interaction.respondEphemeral { content = "A report for this message is just being created, you should see it in a moment" }
            return
        }

        pendingReports.values.firstOrNull { it.message == message }?.let {
            interaction.respondEphemeral {
                content = "Someone filled the modal quicker and got their report here: ${it.reportMessage.url()}"
            }
            return
        }

        queuedReports.add(message)
        interaction.deferEphemeralMessageUpdate()

        interaction.channel.createMessage {
            embed {
                title = "Message Report"
                url = message.url()
                description = "The following message has been reported for breaking the rules"
                field("Reported Message") { message.content }
                field("Reason", true) { interaction.textInputs["reason"]!!.value!! }
                field("Report Score", true) { "0/$REPORT_SCORE_THRESHOLD" }
                field(message.url(), true)
                author {
                    name = author.effectiveName
                    icon = author.avatar?.cdnUrl?.toUrl()
                }
                footer {
                    text = "Reported by ${user.effectiveName}"
                    icon = user.avatar?.cdnUrl?.toUrl()
                }
            }
            actionRow {
                interactionButton(ButtonStyle.Success, "confirm") { label = "Confirm" }
                interactionButton(ButtonStyle.Danger, "reject") { label = "Mark as false" }
            }
        }.let { pendingReports[it.id] = Report(message, it, user) }

        interaction.channel.createMessage(REPORT_ROLE_MENTION).delete()
        synchronized(message) { queuedReports.remove(message) }
    }
}
