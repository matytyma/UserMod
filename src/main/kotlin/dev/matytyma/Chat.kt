package dev.matytyma

fun bold(message: Any) = "**$message**"
fun italic(message: Any) = "*$message*"
fun strikethrough(message: Any) = "~~$message~~"
fun quote(message: Any) = "> ${message.toString().replace("\n", "\n> ")}"
fun code(message: Any) = "`$message`"
fun codeBlock(message: Any, language: String = "") = "```$language\n$message```"
fun spoiler(message: Any) = "||$message||"
fun maskedLink(link: String, display: String) = "[$display](<$link>)"

fun escapeFormatting(message: String) = message
    .replace("*", "\\*")
    .replace("~", "\\~")
    .replace(">", "\\>")
    .replace("`", "\\`")
    .replace("|", "\\|")
    .replace("[", "\\[")
