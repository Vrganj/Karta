package me.vrganj.karta.api.renderer;

import java.awt.image.BufferedImage;

public interface Renderer {

    byte[] render(BufferedImage bufferedImage);
}
