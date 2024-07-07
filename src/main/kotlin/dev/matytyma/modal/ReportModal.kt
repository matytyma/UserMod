package dev.matytyma.modal

import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.*
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed

object ReportModal : ModalExecutor {
    val reportedMessages = mutableMapOf<User, Message>()

    override suspend fun onSubmit(interaction: ModalSubmitInteraction) {
        val deferredResponse = interaction.deferEphemeralResponse()
        val message = reportedMessages.remove(interaction.user) ?: return
        val author = message.author ?: return
        val user = interaction.user

        interaction.channel.createMessage {
            embed {
                title = "Message Report"
                description = "The following message has been reported for breaking the rules"
                field("Reported Message") { message.content }
                field("Reason", true) { interaction.textInputs["reason"]!!.value!! }
                author {
                    name = author.effectiveName
                    icon = author.avatar?.cdnUrl?.toUrl()
                }
                footer {
                    text = "Reported by ${user.effectiveName}"
                    icon = user.avatar?.cdnUrl?.toUrl()
                }
            }
            actionRow {
                interactionButton(ButtonStyle.Success, "confirm") { label = "Confirm" }
                interactionButton(ButtonStyle.Danger, "reject") { label = "Mark as false" }
            }
        }

        deferredResponse.respond { content = "Reported!" }
    }
}