package dev.matytyma.command.admin

import dev.kord.common.entity.PresenceStatus
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.matytyma.*
import dev.matytyma.command.ChatInputCommand
import kotlin.time.measureTime

object ReloadCommand : ChatInputCommand {
    override val description = "Reloads all bot commands"

    override suspend fun register(): ChatInputCreateBuilder.() -> Unit = {
        disableCommandInGuilds()
    }

    override suspend fun onUse(interaction: ChatInputCommandInteraction) {
        val response = interaction.respondEphemeral { content = "Reloading commands..." }
        kord.editPresence { status = PresenceStatus.Invisible }
        val duration = measureTime {
            kord.getGuildApplicationCommands(GUILD_ID).collect { it.delete() }
            loadCommands()
        }
        response.edit { content = "Successfully reloaded in ${code(duration.inWholeMilliseconds)}ms" }
        kord.editPresence { status = PresenceStatus.Online }
    }
}
