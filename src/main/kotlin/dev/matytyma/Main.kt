package dev.matytyma

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.reply
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.matytyma.command.admin.ReloadCommand

lateinit var kord: Kord

val GUILD_ID = Snowflake(System.getenv("GUILD_ID"))

val chatInputCommands = mapOf(
    "reload" to ReloadCommand,
)

suspend fun loadCommands() {
    chatInputCommands.forEach {
        kord.createGuildChatInputCommand(GUILD_ID, it.key, it.value.description, it.value.register())
    }
}

suspend fun main() {
    kord = Kord(System.getenv("BOT_TOKEN"))
    loadCommands()

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        runCatching {
            chatInputCommands[interaction.invokedCommandName]?.onUse(interaction)
        }.onFailure {
            println(it)
            val messageContent = "Ooops, you found a bug...\n${codeBlock(it.stackTraceToString(), "java")}"
            interaction.getOriginalInteractionResponseOrNull()?.reply { content = messageContent }
                ?: interaction.respondEphemeral { content = messageContent }
        }
    }

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}
