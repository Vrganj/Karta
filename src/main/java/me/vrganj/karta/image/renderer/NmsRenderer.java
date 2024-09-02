package me.vrganj.karta.image.renderer;

import net.minecraft.world.level.material.MapColor;

import java.awt.image.BufferedImage;

public class NmsRenderer implements Renderer {

    private final int[] palette;

    public NmsRenderer() {
        palette = new int[MapColor.MATERIAL_COLORS.length * MapColor.Brightness.values().length];

        for (int i = 0; i < palette.length; i++) {
            palette[i] = MapColor.getColorFromPackedId(i);
        }
    }

    @Override
    public byte[] render(BufferedImage bufferedImage) {
        throw new UnsupportedOperationException("NmsRenderer not yet implemented!");
    }
}
