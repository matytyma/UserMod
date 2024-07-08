package dev.matytyma

import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.reply
import dev.kord.core.entity.interaction.ActionInteraction
import dev.kord.core.event.interaction.*
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.matytyma.command.*
import dev.matytyma.command.admin.ReloadCommand
import dev.matytyma.component.button.ButtonExecutor
import dev.matytyma.modal.ModalExecutor
import dev.matytyma.modal.ReportModal
import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()

lateinit var kord: Kord

val GUILD_ID = Snowflake(dotenv["GUILD_ID"])

val chatInputCommands = mapOf<String, ChatInputCommandExecutor>(
    "reload" to ReloadCommand,
)

val messageCommands = mapOf<String, MessageCommandExecutor>(
    "Report" to ReportCommand,
)

val modals = mapOf<String, ModalExecutor>(
    "report" to ReportModal,
)

val buttons = mapOf<String, ButtonExecutor>(

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

suspend fun executeActionInteraction(interaction: ActionInteraction, function: suspend () -> Unit) {
    runCatching { function() }.onFailure {
        println(it)
        val messageContent = "Oops, you found a bug...\n${codeBlock(it.stackTraceToString(), "java")}"
        interaction.getOriginalInteractionResponseOrNull()?.reply { content = messageContent }
            ?: interaction.respondEphemeral { content = messageContent }
    }
}

suspend fun registerEvents() {
    kord.on<ChatInputCommandInteractionCreateEvent> {
        executeActionInteraction(interaction) { chatInputCommands[interaction.invokedCommandName]?.onUse(interaction) }
    }

    kord.on<MessageCommandInteractionCreateEvent> {
        executeActionInteraction(interaction) { messageCommands[interaction.invokedCommandName]?.onUse(interaction) }
    }

    kord.on<ModalSubmitInteractionCreateEvent> {
        executeActionInteraction(interaction) { modals[interaction.modalId]?.onSubmit(interaction) }
    }

    kord.on<ButtonInteractionCreateEvent> {
        executeActionInteraction(interaction) { buttons[interaction.componentId]?.onClick(interaction) }
    }
}

suspend fun main() {
    kord = Kord(dotenv["BOT_TOKEN"])
    loadCommands()
    registerEvents()

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}
