package dev.matytyma.command

import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.MessageCommandInteraction
import dev.matytyma.kord

object ReportCommand : MessageCommand {
    override suspend fun onUse(interaction: MessageCommandInteraction) {
        val deferredResponse = interaction.deferEphemeralResponse()
        val message = interaction.target.asMessage()
        val author = message.author
        val user = interaction.user

        if (author == null || author == kord.getSelf()) {
            deferredResponse.respond { content = "You can't report this message dummy" }
            return
        }

        interaction.channel.createMessage {
            embed {
                title = "Message Report"
                description = "The following message has been reported for breaking the rules"
                field {
                    name = "Reported Message"
                    value =
                        interaction.target.asMessage().content + " https://discord.com/channels/${GUILD_ID}/${message.channelId}/${message.id}"
                }
                author {
                    name = author?.effectiveName
                    icon = author?.avatar?.cdnUrl?.toUrl()
                }
                footer {
                    text = "Reported by ${user.effectiveName}"
                    icon = user.avatar?.cdnUrl?.toUrl()
                }
            }
            actionRow {
                interactionButton(ButtonStyle.Success, "confirm") { label = "Confirm" }
                interactionButton(ButtonStyle.Danger, "reject") { label = "Mark as false" }
                linkButton("https://1.1.1.1") { label = "What's this?" }
            }
        }
        deferredResponse.respond { content = "Reported!" }
    }
}
