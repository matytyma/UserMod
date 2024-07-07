package dev.matytyma.command

import dev.kord.common.entity.TextInputStyle
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.MessageCommandInteraction
import dev.matytyma.kord
import dev.matytyma.modal.ReportModal

object ReportCommand : MessageCommandExecutor {
    override suspend fun onUse(interaction: MessageCommandInteraction) {
        val message = interaction.target.asMessage()
        val author = message.author
        val user = interaction.user

        if (author == null || author == user || author == kord.getSelf()) {
            interaction.respondEphemeral { content = "You can't report this message dummy" }
            return
        }

        interaction.modal("Report a Message", "report") {
            actionRow {
                textInput(TextInputStyle.Short, "reason", "Reason why you are reporting this message") {
                    placeholder = "e.g. Scam"
                    allowedLength = 4..50
                }
            }
        }

        ReportModal.reportedMessages[interaction.user] = message
    }
}
