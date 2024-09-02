package me.vrganj.karta.image.renderer;

import java.awt.image.BufferedImage;

public interface Renderer {

    byte[] render(BufferedImage bufferedImage);
}
