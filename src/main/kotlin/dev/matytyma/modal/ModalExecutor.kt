package dev.matytyma.modal

import dev.kord.core.entity.interaction.ModalSubmitInteraction

interface ModalExecutor {
    suspend fun onSubmit(interaction: ModalSubmitInteraction)
}
