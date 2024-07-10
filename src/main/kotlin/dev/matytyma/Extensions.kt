package dev.matytyma

import dev.kord.core.entity.Message

suspend fun Message.url() = "https://discord.com/channels/${getGuild().id}/${channelId}/$id"
