package me.magikus.core.tools.util;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {

    public static void sendToPlayer(Player p, String text) {
        ((CraftPlayer) p).getHandle().connection.send(new ClientboundSetActionBarTextPacket(new TextComponent(text)));
    }

    public static void sendToAll(String text) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            sendToPlayer(p, text);
        }
    }

    public static void sendScreenMessageToPlayer(Player p, String mainText, String smallText) {
        ((CraftPlayer) p).getHandle().connection.send(new ClientboundSetTitleTextPacket(new TextComponent(mainText)));
        ((CraftPlayer) p).getHandle().connection.send(new ClientboundSetSubtitleTextPacket(new TextComponent(smallText)));
    }

    public static void sendScreenMessageToAll(String bigText, String smallText) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            sendScreenMessageToPlayer(p, bigText, smallText);
        }
    }

}
