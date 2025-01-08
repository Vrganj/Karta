package me.vrganj.karta.api.image.source;

import me.vrganj.karta.api.image.ImageKey;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CompletableFuture;

public interface ImageSource {

    CompletableFuture<BufferedImage> getImage(ImageKey imageKey);

    CompletableFuture<Boolean> exists(ImageKey imageKey);

    File getFile(ImageKey imageKey);

    boolean deleteImage(ImageKey imageKey);
}
