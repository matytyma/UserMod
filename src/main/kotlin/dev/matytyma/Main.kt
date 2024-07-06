package dev.matytyma

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.reply
import dev.kord.core.entity.interaction.ApplicationCommandInteraction
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.GuildMessageCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.matytyma.command.*
import dev.matytyma.command.admin.ReloadCommand
import io.github.cdimascio.dotenv.dotenv
val dotenv = dotenv()

lateinit var kord: Kord

val GUILD_ID = Snowflake(dotenv["GUILD_ID"])

val chatInputCommands = mapOf<String, ChatInputCommand>(
    "reload" to ReloadCommand,
)

val messageCommands = mapOf<String, MessageCommand>(
    "Report" to ReportCommand,
)

suspend fun loadCommands() {
    kord.createGuildApplicationCommands(GUILD_ID) {
        chatInputCommands.forEach {
            val (name, command) = it
            input(name, command.description, command.register())
        }
        messageCommands.forEach {
            val (name, command) = it
            message(name, command.register())
        }
    }
}

suspend fun main() {
    kord = Kord(dotenv["BOT_TOKEN"])
    loadCommands()

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        executeActionInteraction(interaction) {
            chatInputCommands[interaction.invokedCommandName]?.onUse(interaction)
        }
    }

    kord.on<GuildMessageCommandInteractionCreateEvent> {
        executeActionInteraction(interaction) {
            messageCommands[interaction.invokedCommandName]?.onUse(interaction)
        }
    }

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}

suspend fun executeActionInteraction(interaction: ApplicationCommandInteraction, function: suspend () -> Unit) {
    runCatching {
        function()
    }.onFailure {
        println(it)
        val messageContent = "Ooops, you found a bug...\n${codeBlock(it.stackTraceToString(), "java")}"
        interaction.getOriginalInteractionResponseOrNull()?.reply { content = messageContent }
            ?: interaction.respondEphemeral { content = messageContent }
    }
}
