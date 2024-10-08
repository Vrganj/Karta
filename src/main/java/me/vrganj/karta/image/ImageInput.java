package me.vrganj.karta.image;

import me.vrganj.karta.image.renderer.Renderer;
import me.vrganj.karta.image.source.ImageSource;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public record ImageInput(ImageKey imageKey, ImageSource imageSource, Renderer renderer) {

    public CompletableFuture<ImageData> getData() {
        return CompletableFuture.supplyAsync(() -> {
            var image = imageSource.getImage(imageKey).join();
            var resized = new BufferedImage((image.getWidth() + 127) / 128 * 128, (image.getHeight() + 127) / 128 * 128, BufferedImage.TYPE_INT_ARGB);
            resized.getGraphics().drawImage(image, 0, 0, resized.getWidth(), resized.getHeight(), null);
            byte[] data = renderer.render(resized);
            return new ImageData(data, resized.getWidth(), resized.getHeight());
        });
    }
}
