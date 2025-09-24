package gg.uhc.uhc.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

public class ActionBarMessenger {

    public void sendMessage(Player player, String message) {
        sendMessage(Collections.singleton(player), message);
    }

    public void sendMessage(Collection<? extends Player> players, String message) {
        TextComponent component = new TextComponent(message);
        for (Player player : players) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }
    }
}
