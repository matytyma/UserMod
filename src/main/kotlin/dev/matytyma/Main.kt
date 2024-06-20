package dev.matytyma

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.ActionInteractionBehavior
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.reply
import dev.kord.core.event.interaction.*
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.matytyma.command.ReportCommand
import dev.matytyma.command.admin.ReloadCommand

lateinit var kord: Kord

val GUILD_ID = Snowflake(System.getenv("GUILD_ID"))

val chatInputCommands = mapOf(
    "reload" to ReloadCommand,
)

val messageCommands = mapOf(
    "report" to ReportCommand,
)

suspend fun loadCommands() {
    chatInputCommands.forEach {
        kord.createGuildChatInputCommand(GUILD_ID, it.key, it.value.description, it.value.register())
    }

    messageCommands.forEach {
        kord.createGuildMessageCommand(GUILD_ID, it.key, it.value.register())
    }
}

suspend fun main() {
    kord = Kord(System.getenv("BOT_TOKEN"))
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

suspend fun executeActionInteraction(interaction: ActionInteractionBehavior, function: suspend () -> Unit) {
    runCatching {
        function()
    }.onFailure {
        println(it)
        val messageContent = "Ooops, you found a bug...\n${codeBlock(it.stackTraceToString(), "java")}"
        interaction.getOriginalInteractionResponseOrNull()?.reply { content = messageContent }
            ?: interaction.respondEphemeral { content = messageContent }
    }
}
