package me.vrganj.karta.util;

import net.minecraft.core.Direction;
import org.bukkit.Chunk;
import org.bukkit.block.BlockFace;

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

    public static int getDistance(Chunk a, Chunk b) {
        return Math.max(Math.abs(b.getX() - a.getX()), Math.abs(b.getZ() - a.getZ()));
    }
}
