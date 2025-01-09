package me.vrganj.karta.bukkit.util;

import me.vrganj.karta.api.panel.placement.PanelFace;
import net.minecraft.core.Direction;

public class NmsUtil {

    public static Direction fromPanelFace(PanelFace panelFace) {
        return switch (panelFace) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case NORTH -> Direction.NORTH;
            case EAST -> Direction.EAST;
            case SOUTH -> Direction.SOUTH;
            case WEST -> Direction.WEST;
        };
    }
}
