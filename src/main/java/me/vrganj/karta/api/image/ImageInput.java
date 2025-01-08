package me.vrganj.karta.api.image;

import me.vrganj.karta.api.image.source.ImageSource;
import me.vrganj.karta.api.panel.placement.PanelDimensions;
import me.vrganj.karta.api.renderer.Renderer;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public record ImageInput(ImageKey imageKey, ImageSource imageSource, Renderer renderer) {

    public CompletableFuture<ImageData> render(PanelDimensions panelDimensions) {
        return CompletableFuture.supplyAsync(() -> {
            var image = imageSource.getImage(imageKey).join();
            var resized = new BufferedImage(panelDimensions.width() * 128, panelDimensions.height() * 128, BufferedImage.TYPE_INT_ARGB);
            resized.getGraphics().drawImage(image, 0, 0, resized.getWidth(), resized.getHeight(), null);
            byte[] data = renderer.render(resized);
            return new ImageData(data, resized.getWidth(), resized.getHeight());
        });
    }
}
