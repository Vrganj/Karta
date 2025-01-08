package me.vrganj.karta.bukkit.util;

import me.vrganj.karta.api.panel.placement.PanelFace;
import me.vrganj.karta.api.panel.placement.PanelRotation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class NmsUtil {

    public static Rotation fromPanelRotation(PanelRotation panelRotation) {
        return switch (panelRotation) {
            case NONE -> Rotation.NONE;
            case CLOCKWISE_90 -> Rotation.CLOCKWISE_90;
            case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
            case COUNTERCLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
        };
    }

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
