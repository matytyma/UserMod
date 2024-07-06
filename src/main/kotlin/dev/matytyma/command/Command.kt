package dev.matytyma.command

import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.entity.interaction.MessageCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.MessageCommandCreateBuilder

interface ChatInputCommandExecutor {
    val description: String
    suspend fun register(): ChatInputCreateBuilder.() -> Unit = {}
    suspend fun onUse(interaction: ChatInputCommandInteraction)
}

interface MessageCommandExecutor {
    fun register(): MessageCommandCreateBuilder.() -> Unit = {}
    suspend fun onUse(interaction: MessageCommandInteraction)
}
