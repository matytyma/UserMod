package dev.matytyma.component.button

import dev.kord.core.entity.interaction.ButtonInteraction

interface ButtonExecutor {
    suspend fun onClick(interaction: ButtonInteraction)
}
