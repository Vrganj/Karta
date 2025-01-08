package me.vrganj.karta.image.source;

import me.vrganj.karta.image.ImageKey;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

// TODO: cache
public class FileImageSource implements ImageSource {

    private final File dataFolder;

    public FileImageSource(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public File getFile(ImageKey imageKey) {
        return new File(dataFolder, "players" + File.separator + imageKey.ownerId() + File.separator + "images" + File.separator + imageKey.image());
    }

    @Override
    public CompletableFuture<BufferedImage> getImage(ImageKey imageKey) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return ImageIO.read(getFile(imageKey));
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> exists(ImageKey imageKey) {
        return CompletableFuture.supplyAsync(() -> getFile(imageKey).exists());
    }

    @Override
    public boolean deleteImage(ImageKey imageKey) {
        return getFile(imageKey).delete();
    }
}
