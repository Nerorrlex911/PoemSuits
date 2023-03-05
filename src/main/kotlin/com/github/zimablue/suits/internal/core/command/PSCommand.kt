package com.github.zimablue.suits.internal.core.command

import com.github.zimablue.suits.PoemSuits
import org.bukkit.command.CommandSender
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang

@CommandHeader("poemsuits")
object PSCommand {
    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
        }
        incorrectCommand { sender, _, _, _ ->
            sender.sendLang("wrong-command-message")
        }
    }
    @CommandBody(permission = "poemsuits.command.reload")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-reload")
            PoemSuits.reload()
        }
    }

    @CommandBody(permission = "poemsuits.command.help")
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            sender.sendLang("command-message")
        }
    }
}