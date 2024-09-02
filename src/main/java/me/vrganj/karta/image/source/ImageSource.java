package me.vrganj.karta.image.source;

import me.vrganj.karta.image.ImageKey;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

public interface ImageSource {

    CompletableFuture<BufferedImage> getImage(ImageKey imageKey);

    CompletableFuture<Boolean> exists(ImageKey imageKey);
}
