package me.vrganj.karta.util;

import me.vrganj.karta.panel.placement.PanelFace;
import me.vrganj.karta.panel.placement.PanelLocation;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class BukkitUtil {

    public static PanelLocation toPanelLocation(Location location) {
        return new PanelLocation(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static PanelFace toPanelFace(BlockFace face) {
        return switch (face) {
            case UP -> PanelFace.UP;
            case DOWN -> PanelFace.DOWN;
            case NORTH -> PanelFace.NORTH;
            case EAST -> PanelFace.EAST;
            case SOUTH -> PanelFace.SOUTH;
            case WEST -> PanelFace.WEST;
            default -> throw new IllegalArgumentException("Invalid panel face " + face);
        };
    }
}
