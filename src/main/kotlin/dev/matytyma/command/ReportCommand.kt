package dev.matytyma.command

import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.MessageCommandInteraction

object ReportCommand : MessageCommand {
    override suspend fun onUse(interaction: MessageCommandInteraction) {
        println("Reported!")
        interaction.respondEphemeral {
            content = "ayo"
        }
    }
}
