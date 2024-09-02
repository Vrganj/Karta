package me.vrganj.karta.image.renderer;

import org.bukkit.map.MapPalette;

import java.awt.image.BufferedImage;

public class DeprecatedRenderer implements Renderer {

    @Override
    @SuppressWarnings("removal")
    public byte[] render(BufferedImage image) {
        return MapPalette.imageToBytes(image);
    }
}
