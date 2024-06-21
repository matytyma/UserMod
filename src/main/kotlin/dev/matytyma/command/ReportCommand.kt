package dev.matytyma.command

import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.MessageCommandInteraction

object ReportCommand : MessageCommand {
    override suspend fun onUse(interaction: MessageCommandInteraction) {
        val deferredResponse = interaction.deferEphemeralResponse()
        interaction.channel.createEmbed {
            title = "Message Report"
            field {
                name = "Reported Message"
                value = interaction.target.asMessage().content
            }
        }
        deferredResponse.respond { content = "Reported!" }
    }
}
