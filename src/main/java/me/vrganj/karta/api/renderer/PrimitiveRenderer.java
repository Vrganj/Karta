package me.vrganj.karta.api.renderer;

import java.awt.image.BufferedImage;

public class PrimitiveRenderer implements Renderer {

    @Override
    public byte[] render(BufferedImage image) {
        return MapPalette.imageToBytes(image);
    }
}
