package de.jugglegaming.oneWayElytra.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class ActionBar {
    public static void send(Player player, String text){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
    }

    public static void send(Player player, BaseComponent text){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
    }
}
