package me.vrganj.karta.util;

import me.vrganj.karta.Karta;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minecraft.core.Direction;
import org.bukkit.Chunk;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

public class Util {
    private static final BlockFace[] DIRECTIONS = { BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH, BlockFace.EAST };

    public static BlockFace getDirection(float yaw) {
        return DIRECTIONS[Math.round(yaw / 90f) & 0x3];
    }

    public static Direction toDirection(BlockFace face) {
        return Direction.getNearest(face.getModX(), face.getModY(), face.getModZ());
    }

    public static BlockFace getRight(BlockFace face) {
        return BlockFace.values()[(face.ordinal() + 1) % 4];
    }

    public static void send(CommandSender target, String text, TagResolver... tagResolvers) {
        target.sendMessage(Karta.PREFIX.append(MiniMessage.miniMessage().deserialize(text, tagResolvers)));
    }

    public static int getDistance(Chunk a, Chunk b) {
        return Math.max(Math.abs(b.getX() - a.getX()), Math.abs(b.getZ() - a.getZ()));
    }
}
