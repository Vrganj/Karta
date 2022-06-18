package me.vrganj.karta.renderer;

import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageRenderer implements Renderer {
    private final File file;
    private int width = 0;
    private int height = 0;

    public ImageRenderer(File file) {
        this.file = file;
    }

    @Override
    @SuppressWarnings("deprecation")
    public byte[] render(Player player) {
        try {
            var image = ImageIO.read(file);

            width = image.getWidth();
            height = image.getHeight();

            return MapPalette.imageToBytes(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
